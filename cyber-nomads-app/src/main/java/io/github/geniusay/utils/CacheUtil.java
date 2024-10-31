package io.github.geniusay.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Synchronized;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 22:06
 */
public class CacheUtil {

    private static final ConcurrentHashMap<String,String> captchaCache = new ConcurrentHashMap<>();
    private static final Cache<String, String> emailCache = Caffeine.newBuilder()
                                                                .expireAfterWrite(1, TimeUnit.SECONDS)
                                                                .build();
    public static ConcurrentHashMap<String,String> tokenCache = new ConcurrentHashMap<>();

    public static String getCaptcha(String key){
        return captchaCache.get(key);
    }

    public static void putCaptcha(String key,String value){
        captchaCache.put(key,value);
    }

    public static String getEmail(String key){
        return emailCache.getIfPresent(key);
    }

    public static void putEmail(String key,String value){
        emailCache.put(key,value);
    }

    public static void removeCaptcha(String key){
        captchaCache.remove(key);
    }

    public static void removeEmail(String key){
        emailCache.invalidate(key);
    }

    public static String getCaptchaAndRemove(String key) {
        String code = captchaCache.get(key);
        if (code != null) {
            captchaCache.remove(key, code);
        }
        return code;
    }

    public static String getEmailAndRemove(String key) {
        if (emailCache.getIfPresent(key) != null) {
            synchronized (emailCache.getIfPresent(key)) {
                String code = emailCache.getIfPresent(key);
                emailCache.invalidate(key);
                return code;
            }
        } else {
            return null;
        }
    }

    public static boolean isExpired(String key) {
        return emailCache.getIfPresent(key) != null;
    }
}
