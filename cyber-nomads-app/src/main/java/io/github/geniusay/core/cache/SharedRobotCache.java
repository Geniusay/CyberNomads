package io.github.geniusay.core.cache;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.geniusay.constants.RCode;
import io.github.geniusay.constants.RedisConstant;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.mapper.SharedRobotMapper;
import io.github.geniusay.pojo.DO.SharedRobotDO;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SharedRobotCache {
    @Resource
    private RedisTemplate<String, SharedRobotDO> sharedRobotRedisTemplate;

    @Resource
    private SharedRobotMapper sharedRobotMapper;

    // 初始化 hash提供单条查询
    @PostConstruct
    public void init() {
        // 清除老数据
        sharedRobotRedisTemplate.delete(RedisConstant.SHARED_ROBOTS_HASH);
        List<SharedRobotDO> sharedRobotDOS = sharedRobotMapper.selectList(null);
        sharedRobotRedisTemplate.opsForHash().putAll(RedisConstant.SHARED_ROBOTS_HASH,
                sharedRobotDOS.stream().collect(
                        Collectors.toMap(sharedRobotDO -> sharedRobotDO.getRobotId().toString(), sharedRobotDO -> sharedRobotDO)
                )
        );
    }
//    // 分页获取
//    public Page<SharedRobotDO> getSharedRobotsPage(Integer page, Integer size) {
//        return sharedRobotMapper.selectPage(new Page<>(page, size), new QueryWrapper<SharedRobotDO>().orderByDesc("robot_id"));
//    }

    //  添加sharedRobot
    public Boolean putSharedRobot(SharedRobotDO sharedRobotDO) {
        try {
            return sharedRobotRedisTemplate.opsForHash().putIfAbsent(RedisConstant.SHARED_ROBOTS_HASH, sharedRobotDO.getRobotId().toString(), sharedRobotDO);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean exist(Long id){
        return sharedRobotRedisTemplate.opsForHash().hasKey(RedisConstant.SHARED_ROBOTS_HASH, id.toString());
    }

    public SharedRobotDO getSharedRobot(Long id) {
        return (SharedRobotDO) sharedRobotRedisTemplate.opsForHash().get(RedisConstant.SHARED_ROBOTS_HASH, id.toString());
    }

    // 把共享账号分离出来
    public List<Long> getSharedIds(List<Long> ids){
        List<Object> sharedIds = sharedRobotRedisTemplate.executePipelined((RedisCallback<List<Long>>) redisConnection -> {
            List<Long> list = new ArrayList<>();
            Iterator<Long> iterator = ids.iterator();
            Long id;
            while (iterator.hasNext()) {
                id = iterator.next();
                if(Boolean.TRUE.equals(redisConnection.exists(id.toString().getBytes()))){
                    list.add(id);
                    iterator.remove();
                }
            }
            return list;
        });
        return sharedIds.stream().map(id -> (Long)id).collect(Collectors.toList());
    }

    public List<SharedRobotDO> getSharedRobotsById(List<Long> ids){
        return new ArrayList<>();
    }

    // 删除缓存
    public Boolean remove(Long id){
        try {
            return sharedRobotRedisTemplate.opsForHash().delete(RedisConstant.SHARED_ROBOTS_HASH, id.toString()) == 1;
        } catch (Exception e) {
            throw new ServeException(RCode.ERROR);
        }
    }
}
