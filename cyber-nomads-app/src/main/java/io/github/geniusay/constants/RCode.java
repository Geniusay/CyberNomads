package io.github.geniusay.constants;

import lombok.Getter;

/**
 * 描述: 自定义状态码
 * @author suifeng
 * 日期: 2024/7/5
 */
@Getter
public enum RCode {

    // 成功响应
    SUCCESS(200, "成功"),

    /**
     * 失败返回码
     */
    ERROR(400, "服务器繁忙，请稍后重试"),

    NUMBER_OUT(402, "数量上限超出"),

    /**
     * 参数异常
     */
    PARAMS_ERROR(20001, "参数异常或者格式错误"),

    /**
     * 用户权限
     */
    VERIFICATION_FAILURE(30001, "验证失败"),
    FAILED_TO_CREATE_USER(30002, "创建用户失败"),
    MISSING_VERIFICATION_CODE(30003, "缺少验证码"),
    USER_DOES_NOT_EXIST(30004, "用户不存在"),
    PROXY_ERROR(40001, "代理失效"),
    PROXY_BALANCE_ERROR(40002, "代理策略错误");

    /**
     * 自定义状态码
     **/
    private final int code;

    /**
     * 自定义描述
     **/
    private final String message;

    RCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

