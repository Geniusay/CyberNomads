package io.github.geniusay.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.binary.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ConvertorUtil {

    public static <T> T getMap(String key, Map<String, Object> map, Class<T> clazz) {
        Object data = map.get(key);
        if (!Objects.isNull(data)) {
            return clazz.cast(data);
        }
        return null;
    }

    /**
     * 将 Map 转换为 JSON 字符串
     */
    public static String mapToJsonString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return JSON.toJSONString(map);
    }

    /**
     * 将 JSON 字符串转换为 Map
     */
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        return JSONObject.parseObject(jsonString, Map.class);
    }


    public static String listToString(List<Long> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }


    public static List<Long> stringToList(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
