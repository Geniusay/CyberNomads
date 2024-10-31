package io.github.geniusay.utils.AIGenerate;

import java.lang.reflect.Field;

public class ValidUtil {

    /**
     * 判断给定的值是否是指定类中的常量
     */
    public static boolean isValidConstant(Class<?> clazz, String value) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == String.class) {
                    String constantValue = (String) field.get(null);  // 获取静态字段的值
                    if (constantValue.equals(value)) {
                        return true;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法访问常量字段", e);
        }
        return false;
    }
}
