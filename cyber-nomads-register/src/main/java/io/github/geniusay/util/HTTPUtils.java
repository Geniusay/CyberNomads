package io.github.geniusay.util;

import com.alibaba.fastjson.JSON;
import io.github.common.web.Result;
import io.github.geniusay.pojo.VO.RobotVO;
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

    public static Response getWithNullParams(String url, Map<String,String> headers){
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder requestbuilder = new Request.Builder()
                    .url(url)
                    .get();
            for (Map.Entry<String, String> stringObjectEntry : headers.entrySet()) {
                requestbuilder.addHeader(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
            Request request = requestbuilder.build();
            return okHttpClient.newCall(request).execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public static Response postWithParams(String url, Map<String,String> headers, String body) {
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

            return okHttpClient.newCall(request).execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Object convertRespToData(Response response) throws IOException {
        Result result = JSON.parseObject(response.body().string(),Result.class);
        return result.getData();
    }
    public static String convertRespToCode(Response response) throws IOException {
        Result result = JSON.parseObject(response.body().string(),Result.class);
        return result.getCode();
    }
    public static Result convertRespToResult(Response response) throws IOException {
        Result result = JSON.parseObject(response.body().string(),Result.class);
        Result res = new Result();
        res.setCode(result.getCode());
        Object data = result.getData();
        res.setData(Objects.isNull(data)?null:JSON.parseArray(JSON.toJSONString(data), RobotVO.class));
        res.setMsg(result.getMsg());
        res.setTimestamp(result.getTimestamp());
        return res;
    }
}
