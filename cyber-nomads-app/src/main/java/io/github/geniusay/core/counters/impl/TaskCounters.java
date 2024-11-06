package io.github.geniusay.core.counters.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.github.geniusay.core.counters.AbstractCounters;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static io.github.geniusay.constants.CountersConstant.TASK;

@Component(TASK)
public class TaskCounters extends AbstractCounters {
    @Resource
    private TaskMapper taskMapper;

    @Autowired
    public TaskCounters(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected String getKey() {
        return "cyber:task:count";
    }

    @Override
    protected synchronized void init() {
        redisTemplate.opsForHash().put(getKey(), ThreadUtil.getUid(), taskMapper.selectCount(new LambdaUpdateWrapper<TaskDO>().eq(TaskDO::getUid, ThreadUtil.getUid())) + 1);
    }

    @Override
    protected String getType() {
        return TASK;
    }
}
