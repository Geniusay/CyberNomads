package io.github.geniusay.util;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 21:16
 */
public class CacheUtils {
    public static final Set<String> robots = new HashSet<>();
    public static String key = "";
    public static String version = "";
    public static String browserName = "";
    public static String path = "";
}
