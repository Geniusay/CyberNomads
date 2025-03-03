package io.github.geniusay.core.ai.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.github.geniusay.core.ai.config.QwConfig;
import io.github.geniusay.core.ai.core.AIModel;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 描述: 千问模型
 * @author suifeng
 * 日期: 2025/3/3
 */
@Component
public class QwModel implements AIModel<QwConfig> {

    private QwConfig config;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void init(QwConfig config) {
        this.config = config;
    }

    @Override
    public String generate(String prompt) {
        try {
            // 构造请求体
            HttpURLConnection connection = createConnection(prompt);

            // 读取响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // 解析响应结果
            JSONObject jsonObject = JSONObject.parseObject(response.toString());
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content");
        } catch (Exception e) {
            throw new RuntimeException("调用QW模型失败: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T generate(String prompt, Class<T> responseType) {
        return null;
    }

    private HttpURLConnection createConnection(String content) throws IOException {
        RequestBody requestBody = new RequestBody(
                config.getVersion(),
                new Message[]{
                        new Message("system", config.getSystem()),
                        new Message("user", content)
                }
        );

        Gson gson = new Gson();
        String jsonInputString = gson.toJson(requestBody);

        // 创建HTTP连接
        URL url = new URL(config.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + config.getApiKey());
        connection.setDoOutput(true);

        // 发送请求体
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }

    // 内部类：消息结构
    static class Message {
        String role;
        String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    // 内部类：请求体结构
    static class RequestBody {
        String model;
        Message[] messages;

        public RequestBody(String model, Message[] messages) {
            this.model = model;
            this.messages = messages;
        }
    }
}
