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

    private final String platform;
    private final Integer code;

    // 方法移动至PlatformUtil
}
