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
import java.net.URL;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 0:39
 */
@Component("qw")
public class qwAI implements BaseGenerate {

    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    private String send(String text,String API_KEY,Integer num) {
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
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {

                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // 获取响应
            int responseCode = connection.getResponseCode();
            if(responseCode!=200){
                throw new ServeException(500,"AI接口调用失败");
            }

            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

}
