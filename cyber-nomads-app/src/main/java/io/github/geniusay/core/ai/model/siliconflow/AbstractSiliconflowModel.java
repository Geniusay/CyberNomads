package io.github.geniusay.core.ai.model.siliconflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.github.geniusay.core.ai.config.SiliconflowConfig;
import io.github.geniusay.core.ai.core.AIModel;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 描述: DeepSeek模型
 * @author suifeng
 * 日期: 2025/3/3
 */
@RequiredArgsConstructor
public abstract class AbstractSiliconflowModel implements AIModel {
    
    private final SiliconflowConfig config;

    @Override
    public String generate(String prompt) {
        try {
            HttpURLConnection connection = createConnection(prompt);
            
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            
            JSONObject jsonResponse = JSONObject.parseObject(response.toString());
            return extractContent(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("DeepSeek API调用失败: " + e.getMessage(), e);
        }
    }

    private String extractContent(JSONObject jsonResponse) {
        JSONArray choices = jsonResponse.getJSONArray("choices");
        if (choices != null && !choices.isEmpty()) {
            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            return message.getString("content");
        }
        return "";
    }

    @Override
    public <T> T generate(String prompt, Class<T> responseType) {
        return null;
    }

    protected HttpURLConnection createConnection(String content) throws Exception {
        URL url = new URL(config.getBaseUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + config.getApiKey());
        conn.setDoOutput(true);

        RequestBody requestBody = new RequestBody(
            getModelVersion(),
            new Message[] { new Message("user", content) },
            config.getMaxTokens(),
            config.getTemperature()
        );

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = new Gson().toJson(requestBody).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }

    // 内部数据结构
    @lombok.Data
    private static class RequestBody {
        private final String model;
        private final Message[] messages;
        private final Integer max_tokens;
        private final Double temperature;
        private final boolean stream = false;
    }

    @lombok.Data
    private static class Message {
        private final String role;
        private final String content;
    }

    protected abstract String getModelVersion();
}