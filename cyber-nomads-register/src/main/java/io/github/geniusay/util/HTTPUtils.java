package io.github.geniusay.util;

import com.alibaba.fastjson.JSON;
import io.github.common.web.Result;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:18
 */
public class HTTPUtils {

    public static Object getWithNullParams(String url, Map<String,String> headers){
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder requestbuilder = new Request.Builder()
                    .url(url)
                    .get();
            for (Map.Entry<String, String> stringObjectEntry : headers.entrySet()) {
                requestbuilder.addHeader(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
            Request request = requestbuilder.build();
            Response response = okHttpClient.newCall(request).execute();
            String body = response.body().string();
            return convertRespToObj(body);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public static Object postWithParams(String url, Map<String,String> headers, String body) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
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

            return convertRespToObj(response.body().string());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static Object convertRespToObj(String response) {
        Result result = JSON.parseObject(response,Result.class);
        System.out.println(result);
        return result.getData();
    }
}
