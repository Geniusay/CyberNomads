package io.github.geniusay.utils;

import java.util.Map;
import java.util.Objects;

public class FormatUtil {

    public static <T> T getMap(String key, Map<String, Object> map, Class<T> clazz) {
        Object data = map.get(key);
        if (!Objects.isNull(data)) {
            return clazz.cast(data);
        }
        return null;
    }
}
