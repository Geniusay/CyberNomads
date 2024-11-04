package io.github.geniusay.pojo;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.config.TaskPlatformConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 12:09
 */
@Getter
@AllArgsConstructor
public enum Platform {

    BILIBILI(TaskPlatformConstant.BILIBILI,1),
    DOUYIN(TaskPlatformConstant.DOUYING,2);
    private String platform;
    private Integer code;

    public static String getPlatformByCode(int code){
        for (Platform platform : Platform.values()) {
            if (platform.code.equals(code)) {
                return platform.platform;
            }
        }
        throw new ServeException(500,"不支持的平台类型");
    }
}
