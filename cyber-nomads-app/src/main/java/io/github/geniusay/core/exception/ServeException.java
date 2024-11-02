package io.github.geniusay.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述: 全局业务异常类
 * @author suifeng
 * 日期: 2024/7/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServeException extends RuntimeException {

    /**
     * 状态码
     */
    private int code = RCode.ERROR.getCode();

    /**
     * 异常消息
     */
    private String message = RCode.ERROR.getMessage();


    public ServeException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServeException(String message) {
        this.message = message;
    }

    public ServeException(int code) {
        this.code = code;
    }

    public ServeException(RCode rCode) {
        this.code = rCode.getCode();
        this.message = rCode.getMessage();
    }

    public ServeException(RCode rCode, String addMessage) {
        this.code = rCode.getCode();
        this.message = rCode.getMessage() + addMessage;
    }

    public static ServeException of(int code,String msg) {
        return new ServeException(code,msg);
    }
    public static ServeException of(ServeCode serveCode) {
        return ServeException.of(Integer.parseInt(serveCode.getCode()),serveCode.getMsg());
    }

    public static ServeException of(ServeCode serveCode,String msg) {
        String newMsg = String.format(serveCode.getMsg(), msg);
        return ServeException.of(Integer.parseInt(serveCode.getCode()),newMsg);
    }

    public static ServeException SystemError(String msg){
        return ServeException.of(RCode.ERROR.getCode(),msg);
    }
}
