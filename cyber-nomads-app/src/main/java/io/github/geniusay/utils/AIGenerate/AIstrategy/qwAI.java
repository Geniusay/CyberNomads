package io.github.geniusay.utils.AIGenerate.AIstrategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.utils.AIGenerate.BaseGenerate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 0:39
 */
@Component("qw")
public class qwAI implements BaseGenerate {

    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    private String send(String text, String API_KEY, Integer num) {
        try {
            RequestBody requestBody = new RequestBody(
                    "qwen-plus",
                    new Message[]{
                            new Message("system", "You are a helpful assistant."),
                            new Message("user", text+"，字数控制在"+num+"以内，同时以纯文本形式发送")
                    }
            );

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(requestBody);

            URL url = new URL(API_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            String auth = "Bearer " + API_KEY;
            httpURLConnection.setRequestProperty("Authorization", auth);

            httpURLConnection.setDoOutput(true);
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = httpURLConnection.getResponseCode();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {

                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String sendAndReturnString(String text, String key, Integer num) {
        text = text.replaceAll("[\\n\\r]+", " ").trim();
        String res = this.send(text, key, num);
        JSONObject jsonObject = JSONObject.parseObject(res);
        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        return message.getString("content");
    }

    // 请为我们的项目“Cybernomads（赛博游民）”创作幽默风趣且高度抽象的推广语。赛博游民是一款全自动回复评论的软件，支持多任务、多账号及一键式自动化功能。推广语需融入中国网络热梗，运用隐喻、象征等修辞手法，保持足够的抽象和隐晦，使其难以辨识为广告或AI生成内容。确保推广语新颖有趣、易于传播，能够引发大众共鸣。思维需高度发散，联想到世间万物，打破传统框架，比普通互联网用户的表达更具抽象性。生成内容应简洁有力，无空行，能够瞬间吸引目光。

    static class Message {
        String role;
        String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    static class RequestBody {
        String model;
        Message[] messages;

        public RequestBody(String model, Message[] messages) {
            this.model = model;
            this.messages = messages;
        }
    }


}
