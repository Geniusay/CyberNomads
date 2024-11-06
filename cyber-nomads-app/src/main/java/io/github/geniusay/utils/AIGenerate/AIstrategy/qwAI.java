package io.github.geniusay.utils.AIGenerate.AIstrategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.utils.AIGenerate.BaseGenerate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 0:39
 */
@Component("qw")
public class qwAI implements BaseGenerate {

    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    @Override
    public String send(String text,String API_KEY,Integer num) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String jsonInputString = "{"
                    + "\"model\": \"qwen-plus\","
                    + "\"messages\": ["
                    + "{"
                    + "\"role\": \"system\","
                    + "\"content\": \"You are a helpful assistant.\""
                    + "},"
                    + "{"
                    + "\"role\": \"user\","
                    + "\"content\": \""+ text+"。请控制字数在"+num+"字内，以纯文本的形式回答" + "\""
                    + "}"
                    + "]"
                    + "}";

            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 获取响应
            int responseCode = connection.getResponseCode();
            if(responseCode!=200){
                throw new ServeException(500,"AI接口调用失败");
            }
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {

                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
