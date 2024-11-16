package io.github.geniusay.config;

import io.github.common.web.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/15 21:50
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(Exception ex) {
        return Result.error("500", ex.getMessage());
    }

}
