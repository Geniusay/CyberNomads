package io.github.geniusay.crawler.util.bilibili;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BilibiliUtils {

    /**
     * 从Cookie中提取csrf（bili_jct）
     *
     * @param cookie 用户的Cookie
     * @return String 提取的csrf值
     */
    public static String extractCsrfFromCookie(String cookie) {
        // 使用正则表达式提取bili_jct的值
        Pattern pattern = Pattern.compile("bili_jct=([^;]+)");
        Matcher matcher = pattern.matcher(cookie);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;  // 如果没有找到bili_jct，返回null
    }

    /**
     * 解析发送评论的响应
     *
     * @param response JSON响应
     * @return boolean 是否发送成功
     */
    public static boolean parseSendCommentResponse(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        return code == 0;  // code为0表示成功
    }

    /**
     * 解析评论点赞或点踩的响应
     *
     * @param response JSON响应
     * @return boolean 是否操作成功
     */
    public static boolean parseLikeOrDislikeResponse(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        return code == 0;  // code为0表示成功
    }

    /**
     * 解析点赞或取消点赞的响应
     *
     * @param response JSON响应
     * @return boolean 是否操作成功
     */
    public static boolean parseLikeVideoResponse(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        return code == 0;  // code为0表示成功
    }
}
