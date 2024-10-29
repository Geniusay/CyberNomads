package io.github.geniusay.crawler.util.bilibili;

import io.github.geniusay.crawler.po.bilibili.Barrage;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BilibiliUtil {

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


    @NotNull
    public static ApiResponse<Boolean> getBooleanApiResponse(String cookie, FormBody.Builder formBuilder, String sendCommentUrl) {
        RequestBody formBody = formBuilder.build();

        try {
            // 发送POST请求
            ApiResponse<String> response = HttpClientUtil.sendPostRequest(sendCommentUrl, formBody, cookie);
            return ApiResponse.handleApiResponse(response, Boolean.class, r -> true);
        } catch (IOException e) {
            return ApiResponse.errorResponse(e);
        }
    }

    /**
     * 解析弹幕XML数据并返回弹幕列表
     *
     * @param xmlData 弹幕的XML数据
     * @return List<Barrage> 弹幕列表
     */
    public static List<Barrage> parseBarrageXml(String xmlData) {
        List<Barrage> barrageList = new ArrayList<>();

        try {
            // 创建XML解析器
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
            Document doc = builder.parse(is);

            // 获取所有<d>标签（弹幕）
            NodeList nodeList = doc.getElementsByTagName("d");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                // 获取 p 属性并解析
                String[] pAttributes = element.getAttribute("p").split(",");

                // 获取弹幕内容
                String content = element.getTextContent();

                // 使用Barrage类的静态方法创建Barrage对象
                Barrage barrage = Barrage.fromAttributes(pAttributes, content);

                // 加入列表
                barrageList.add(barrage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return barrageList;
    }
}
