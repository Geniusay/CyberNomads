package io.github.geniusay.utils;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.pojo.Platform;

import java.util.HashMap;

public class PlatformUtil {
    private static final HashMap<Integer, String> platformMap = new HashMap<>(Platform.values().length);

    static {
        for (Platform platform : Platform.values()) {
            platformMap.put(platform.getCode(), platform.getPlatform());
        }
    }

    public static String getPlatformByCode(int code){
        return platformMap.computeIfAbsent(code, k ->{
            throw new ServeException(500,"不支持的平台类型");
        });
    }

    public static void checkPlatform(int code){
        if(!platformMap.containsKey(code)){
            throw new ServeException(500,"不支持的平台类型");
        }
    }
}
