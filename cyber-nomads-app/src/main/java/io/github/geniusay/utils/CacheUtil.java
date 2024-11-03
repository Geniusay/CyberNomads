package io.github.geniusay.utils;

import io.github.geniusay.constants.RedisConstant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 22:06
 */
@Component
public class CacheUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String getCaptcha(String key){
        return stringRedisTemplate.opsForValue().get(RedisConstant.PIC_CAPTCHA + key);
    }

    public void putCaptcha(String key,String value){
        stringRedisTemplate.opsForValue().set(RedisConstant.PIC_CAPTCHA + key, value, 60, TimeUnit.SECONDS);
    }

    public void removeCaptcha(String key){
        stringRedisTemplate.delete(RedisConstant.PIC_CAPTCHA + key);
    }

    public String getCaptchaAndRemove(String key) {
        String code = stringRedisTemplate.opsForValue().get(RedisConstant.PIC_CAPTCHA + key);
        if (code != null) {
            stringRedisTemplate.delete(RedisConstant.PIC_CAPTCHA + key);
        }
        return code;
    }

    public String getEmail(String key){
        return stringRedisTemplate.opsForValue().get(RedisConstant.EMAIL_CAPTCHA + key);
    }

    public void putEmail(String key,String value){
        stringRedisTemplate.opsForValue().set(RedisConstant.EMAIL_CAPTCHA + key, value, 60, TimeUnit.SECONDS);
    }

    public void removeEmail(String key){
        stringRedisTemplate.delete(RedisConstant.EMAIL_CAPTCHA + key);
    }

    public String getEmailAndRemove(String key) {
        String code = stringRedisTemplate.opsForValue().get(RedisConstant.EMAIL_CAPTCHA + key);
        if (code != null) {
            stringRedisTemplate.delete(RedisConstant.EMAIL_CAPTCHA + key);
        }
        return code;
    }

    public boolean emailCodeIsExpired(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisConstant.EMAIL_CAPTCHA + key));
    }
}
