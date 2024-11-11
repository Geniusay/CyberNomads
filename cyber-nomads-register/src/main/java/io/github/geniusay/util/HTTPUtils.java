package io.github.geniusay.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:18
 */
public class HTTPUtils {

    public static void getWithNullParams(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);

    }


    public static void postWithParams(String url, Map<String,String> headers,String body) {
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            Request.Builder requestbuilder = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, body));
            //增加请求头
            for (Map.Entry<String, String> stringObjectEntry : headers.entrySet()) {
                requestbuilder.addHeader(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }

            Request request = requestbuilder.build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();
            System.out.println(response.code());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
