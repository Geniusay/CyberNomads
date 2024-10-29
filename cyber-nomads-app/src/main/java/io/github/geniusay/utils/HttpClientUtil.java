package io.github.geniusay.utils;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.*;

import static io.github.geniusay.utils.DecompressUtil.decompressDeflateStream;

public class HttpClientUtil {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0";

    /**
     * 发送GET请求，并处理deflate压缩的响应数据
     *
     * @param url    请求的URL
     * @param cookie 请求中需要的cookie
     * @return 解压后的响应体字符串
     * @throws IOException
     */
    public static String sendGetRequestWithDeflate(String url, String cookie) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Cookie", cookie)
                .addHeader("Accept-Encoding", "deflate")  // 明确只接受deflate编码
                .addHeader("User-Agent", DEFAULT_USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 获取响应体的字节流
            InputStream inputStream = response.body().byteStream();

            // 调用手动解压方法
            return decompressDeflateStream(inputStream);
        }
    }

    /**
     * 发送POST请求
     *
     * @param url 请求的URL
     * @param formBody 请求的表单数据
     * @param cookie 用户的Cookie
     * @return 响应体字符串
     * @throws IOException
     */
    public static String sendPostRequest(String url, RequestBody formBody, String cookie) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", DEFAULT_USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 发送GET请求
     * 
     * @param url 请求的URL
     * @param cookie 用户的Cookie
     * @return 响应体字符串
     * @throws IOException
     */
    public static String sendGetRequest(String url, String cookie) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", DEFAULT_USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     * 解析JSON字符串为Java对象
     * 
     * @param json JSON字符串
     * @param clazz 目标类
     * @param <T> 泛型类型
     * @return 解析后的对象
     */
    public static <T> T parseJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }
}