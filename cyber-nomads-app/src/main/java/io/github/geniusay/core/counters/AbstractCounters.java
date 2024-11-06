package io.github.geniusay.core.counters;

import io.github.geniusay.constants.RCode;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.utils.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class AbstractCounters{
    private static final Logger log = LoggerFactory.getLogger(AbstractCounters.class);
    protected final RedisTemplate<String, Long> redisTemplate;

    public AbstractCounters(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void increment() {
        try {
            if(redisTemplate.opsForHash().hasKey(getKey(), ThreadUtil.getUid())){
                init();
            } else {
                redisTemplate.opsForHash().increment(getKey(), ThreadUtil.getUid(), 1);
            }
        } catch (Exception e) {
            log.info("{} {} limit", ThreadUtil.getUid(), getType());
            throw new ServeException(RCode.NUMBER_OUT);
        }
    }

    protected abstract String getKey();

    protected abstract void init();

    protected abstract String getType();
}