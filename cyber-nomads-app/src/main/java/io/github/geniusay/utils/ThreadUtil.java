package io.github.geniusay.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/28 20:12
 */
public class ThreadUtil {

    private static ThreadLocal<Map<String, String>> threadLocal = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, String value) {
        threadLocal.get().put(key, value);
    }

    public static String get(String key) {
        return threadLocal.get().get(key);
    }

    public static String getUid(){
        String uid = threadLocal.get().get("uid");
        if(uid == null) throw new RuntimeException("uid为null");
        return uid;
    }

    public static String getUsername(){
        String username = threadLocal.get().get("username");
        if(username == null) throw new RuntimeException("username为null");
        return username;
    }

    public static void remove() {
        threadLocal.remove();
    }

}
