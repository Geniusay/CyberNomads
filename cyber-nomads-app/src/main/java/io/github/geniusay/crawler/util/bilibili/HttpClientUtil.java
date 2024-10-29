package io.github.geniusay.crawler.util.bilibili;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;

import static io.github.geniusay.utils.DecompressUtil.decompressDeflateStream;

public class HttpClientUtil {

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0";

    private static OkHttpClient defaultClient = new OkHttpClient();

    /**
     * 获取 OkHttpClient 实例
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getClient() {
        // 这里可以返回代理池的 OkHttpClient 实例
        // 目前直接返回本地的默认 OkHttpClient
        return defaultClient;
    }

    /**
     * 发送GET请求，并处理deflate压缩的响应数据
     *
     * @param url    请求的URL
     * @param cookie 请求中需要的cookie
     * @return ApiResponse<String> 解压后的响应体字符串
     * @throws IOException
     */
    public static ApiResponse<String> sendGetRequestWithDeflate(String url, String cookie) throws IOException {
        long requestTimestamp = System.currentTimeMillis(); // 记录请求时间

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Cookie", cookie)
                .addHeader("Accept-Encoding", "deflate")  // 明确只接受deflate编码
                .addHeader("User-Agent", DEFAULT_USER_AGENT)
                .build();

        try (Response response = getClient().newCall(request).execute()) {
            long responseTimestamp = System.currentTimeMillis(); // 记录响应时间
            long duration = responseTimestamp - requestTimestamp; // 计算耗时

            if (!response.isSuccessful()) {
                return new ApiResponse<>(response.code(), "HTTP请求失败: " + response.message(), false, null, requestTimestamp, responseTimestamp, duration);
            }

            // 获取响应体的字节流
            InputStream inputStream = response.body().byteStream();

            // 调用手动解压方法
            String decompressedResponse = decompressDeflateStream(inputStream);

            // 返回成功的 ApiResponse
            return new ApiResponse<>(0, "Success", true, decompressedResponse, requestTimestamp, responseTimestamp, duration);
        }
    }

    /**
     * 发送GET请求
     *
     * @param url    请求的URL
     * @param cookie 用户的Cookie
     * @return ApiResponse<String> 响应体字符串
     * @throws IOException
     */
    public static ApiResponse<String> sendGetRequest(String url, String cookie) throws IOException {
        long requestTimestamp = System.currentTimeMillis(); // 记录请求时间

        cookie = cookie == null ? "" : cookie;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", DEFAULT_USER_AGENT)
                .build();

        return getStringApiResponse(requestTimestamp, request);
    }

    /**
     * 发送POST请求
     *
     * @param url 请求的URL
     * @param formBody 请求的表单数据
     * @param cookie 用户的Cookie
     * @return ApiResponse<String> 响应体字符串
     * @throws IOException
     */
    public static ApiResponse<String> sendPostRequest(String url, RequestBody formBody, String cookie) throws IOException {
        long requestTimestamp = System.currentTimeMillis(); // 记录请求时间

        cookie = cookie == null ? "" : cookie;
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", DEFAULT_USER_AGENT)
                .build();

        return getStringApiResponse(requestTimestamp, request);
    }

    @NotNull
    public static ApiResponse<String> getStringApiResponse(long requestTimestamp, Request request) throws IOException {
        try (Response response = getClient().newCall(request).execute()) {
            long responseTimestamp = System.currentTimeMillis(); // 记录响应时间
            long duration = responseTimestamp - requestTimestamp; // 计算耗时

            if (!response.isSuccessful()) {
                return new ApiResponse<>(response.code(), "HTTP请求失败: " + response.message(), false, null, requestTimestamp, responseTimestamp, duration);
            }

            String responseBody = response.body().string();
            // 解析B站返回的JSON响应
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
            int code = jsonObject.get("code").getAsInt();
            String message = jsonObject.get("message").getAsString();

            // 判断B站返回的业务逻辑是否成功
            if (code == 0) {
                return new ApiResponse<>(code, "Success", true, responseBody, requestTimestamp, responseTimestamp, duration);
            } else {
                return new ApiResponse<>(code, message, false, responseBody, requestTimestamp, responseTimestamp, duration);
            }
        }
    }
}