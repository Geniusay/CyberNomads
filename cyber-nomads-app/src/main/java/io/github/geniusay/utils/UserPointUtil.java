package io.github.geniusay.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.github.geniusay.mapper.PointRecordMapper;
import io.github.geniusay.mapper.UserMapper;
import io.github.geniusay.pojo.DO.PointRecord;
import io.github.geniusay.pojo.DO.UserDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserPointUtil {
    @Resource
    private PointRecordMapper pointRecordMapper;

    @Resource
    private UserMapper userMapper;

    private final ConcurrentHashMap<String, AtomicInteger> pointCache = new ConcurrentHashMap<>();

    private final Object lock = new Object();

    public boolean reduce(Integer point, String option){
        return option(PointRecord.builder()
                .uid(ThreadUtil.getUid())
                .pointOption(option)
                .point(-point)
                .build());
    }

    public boolean increase(Integer point, String option){
        return option(PointRecord.builder()
                .uid(ThreadUtil.getUid())
                .pointOption(option)
                .point(point)
                .build());
    }

    public void resetUserPoint(String uid){
        pointCache.put(uid, new AtomicInteger(queryDatabase(uid)));
    }

    private boolean option(PointRecord record){
        if(option(record.getPoint())){
            pointRecordMapper.insert(record);
            LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(UserDO::getUid, record.getUid())
                    .set(UserDO::getPoint, getPoint().get());
            userMapper.update(null, wrapper);
            return true;
        }
        return false;
    }

    private AtomicInteger getPoint(){
        String uid = ThreadUtil.getUid();
        return pointCache.computeIfAbsent(uid, k -> {
            synchronized (lock) {
                return new AtomicInteger(queryDatabase(k));
            }
        });
    }

    public Integer get(){
        return getPoint().get();
    }

    private boolean option(Integer point){
        AtomicInteger balance = getPoint();
        int currentBalance;
        while((currentBalance = balance.get()) >= -point){
            if(balance.compareAndSet(currentBalance, currentBalance + point))
                return true;
        }
        return false;
    }

    private Integer queryDatabase(String uid){
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserDO::getPoint)
                .eq(UserDO::getUid,uid);
        return userMapper.selectOne(wrapper).getPoint();
    }
}
