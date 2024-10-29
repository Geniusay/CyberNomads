package io.github.geniusay.crawler.util.bilibili;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 描述: 接口响应包装类
 * @author suifeng
 * 日期: 2024/10/29
 */
@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private int code;              // 状态码
    private String msg;        // 消息
    private boolean success;       // 是否成功
    private T data;                // 数据
    private long reqTime; // 请求发送的时间戳
    private long respTimes; // 得到响应的时间戳
    private long dur;         // 响应耗时（毫秒）

    /**
     * 将 ApiResponse<String> 转换为 ApiResponse<T>
     *
     * @param response ApiResponse<String> 原始响应
     * @param clazz    目标类型的类
     * @param <T>      目标类型
     * @return ApiResponse<T> 转换后的响应
     */
    public static <T> ApiResponse<T> convertApiResponse(ApiResponse<String> response, Class<T> clazz) {
        if (response.isSuccess()) {
            // 使用 Gson 将字符串转换为目标类型
            T data = new Gson().fromJson(response.getData(), clazz);
            return new ApiResponse<>(response.getCode(), response.getMsg(), true, data, response.getReqTime(), response.getRespTimes(), response.getDur());
        } else {
            // 如果失败，返回一个失败的 ApiResponse
            return new ApiResponse<>(response.getCode(), response.getMsg(), false, null, response.getReqTime(), response.getRespTimes(), response.getDur());
        }
    }

    /**
     * 将 ApiResponse<String> 转换为 ApiResponse<T>，并处理成功和失败的情况
     *
     * @param response ApiResponse<String> 原始响应
     * @param clazz    目标类型的类
     * @param <T>      目标类型
     * @param parser   用于处理非标准的 JSON 数据（如评论解析）
     * @return ApiResponse<T> 转换后的响应
     */
    public static <T> ApiResponse<T> handleApiResponse(ApiResponse<String> response, Class<T> clazz, ResponseParser<T> parser) {
        if (response.isSuccess()) {
            // 如果有自定义的解析器，使用它解析
            T data = (parser != null) ? parser.parse(response.getData()) : new Gson().fromJson(response.getData(), clazz);
            return new ApiResponse<>(response.getCode(), response.getMsg(), true, data, response.getReqTime(), response.getRespTimes(), response.getDur());
        } else {
            // 如果失败，返回一个失败的 ApiResponse
            return new ApiResponse<>(response.getCode(), response.getMsg(), false, null, response.getReqTime(), response.getRespTimes(), response.getDur());
        }
    }


    /**
     * 创建一个错误的 ApiResponse
     *
     * @param message 错误信息
     * @param <T>     数据类型
     * @return ApiResponse<T> 错误响应
     */
    public static <T> ApiResponse<T> errorResponse(String message) {
        long currentTime = System.currentTimeMillis();
        return new ApiResponse<>(-1, message, false, null, currentTime, currentTime, 0);
    }

    /**
     * 创建一个错误的 ApiResponse，并包含异常信息
     *
     * @param exception 异常对象
     * @param <T>       数据类型
     * @return ApiResponse<T> 错误响应
     */
    public static <T> ApiResponse<T> errorResponse(Exception exception) {
        return errorResponse("程序内部错误: " + exception.getMessage());
    }
}