package io.github.geniusay.utils;

import lombok.Synchronized;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 22:06
 */
public class CacheUtil {

    private static ConcurrentHashMap<String,String> captchaCache = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,String> emailCache = new ConcurrentHashMap<>();

    public static String getCaptcha(String key){
        return captchaCache.get(key);
    }

    public static void putCaptcha(String key,String value){
        captchaCache.put(key,value);
    }

    public static String getEmail(String key){
        return emailCache.get(key);
    }

    public static void putEmail(String key,String value){
        emailCache.put(key,value);
    }

    public static void removeCaptcha(String key){
        captchaCache.remove(key);
    }

    public static void removeEmail(String key){
        emailCache.remove(key);
    }

    public static String getCaptchaAndRemove(String key) {
        if(captchaCache.get(key)!=null){
            synchronized (captchaCache.get(key)) {
                String code = captchaCache.get(key);
                captchaCache.remove(key);
                return code;
            }
        }else{
            return null;
        }
    }

    public static String getEmailAndRemove(String key){
        if(emailCache.get(key)!=null){
            synchronized (captchaCache.get(key)) {
                String code = captchaCache.get(key);
                captchaCache.remove(key);
                return code;
            }
        }else{
            return null;
        }
    }

}
