package io.github.geniusay.utils.AIGenerate.AIstrategy;

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
    public String send(String text,String API_KEY) {
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
                    + "\"content\": \""+ text +"\""
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
            System.out.println("Response Code: " + responseCode);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Response: " + response);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
