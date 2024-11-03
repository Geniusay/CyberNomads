package io.github.geniusay.core.exception;

import io.github.geniusay.constants.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 描述: 全局异常捕获
 * @author suifeng
 * 日期: 2024/7/5
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    /**
     * 自定义异常类
     */
    @ExceptionHandler(ServeException.class)
    public R<Object> handleServeException(final ServeException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public R<Object> runtimeExceptionHandler(final Exception e) {
        e.printStackTrace();
        log.error("全局异常[RuntimeException]: {}:", e.getMessage());
        return R.error();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        // 获取校验错误信息
        return R.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<String> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        return R.error(RCode.PARAMS_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    public R<String> illegalStateExceptionHandler(IllegalStateException e) {
        log.error(e.getMessage());
        return R.error(RCode.ERROR);
    }
}
