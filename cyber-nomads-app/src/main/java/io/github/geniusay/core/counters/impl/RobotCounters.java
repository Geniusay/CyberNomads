package io.github.geniusay.core.counters.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.github.geniusay.core.counters.AbstractCounters;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static io.github.geniusay.constants.CountersConstant.ROBOT;

@Component(ROBOT)
public class RobotCounters extends AbstractCounters {
    @Resource
    private RobotMapper robotMapper;

    @Autowired
    public RobotCounters(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getKey() {
        return "cyber:robot:count";
    }

    @Override
    protected synchronized void init() {
        if (redisTemplate.opsForHash().hasKey(getKey(), ThreadUtil.getUid())){
            redisTemplate.opsForHash().put(getKey(), ThreadUtil.getUid(), robotMapper.selectCount(new LambdaUpdateWrapper<RobotDO>().eq(RobotDO::getUid, ThreadUtil.getUid())) + 1);
        }
    }

    @Override
    protected String getType() {
        return ROBOT;
    }
}