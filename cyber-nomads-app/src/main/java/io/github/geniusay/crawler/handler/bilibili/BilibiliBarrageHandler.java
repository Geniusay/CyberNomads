package io.github.geniusay.crawler.handler.bilibili;

import io.github.geniusay.crawler.po.bilibili.Barrage;
import io.github.geniusay.utils.HttpClientUtil;
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

/**
 * 描述: B站弹幕相关爬虫处理器
 * 日期: 2024/10/29
 */
public class BilibiliBarrageHandler {

    // 实时弹幕的URL
    private static final String REAL_TIME_BARRAGE_URL = "https://comment.bilibili.com/";

    /**
     * 获取视频的实时弹幕池弹幕
     *
     * @param cid 视频的cid / 弹幕池id
     * @return List<Barrage> 弹幕列表
     */
    public static List<Barrage> getRealTimeBarrageByCid(String cookie, String cid) {
        String url = REAL_TIME_BARRAGE_URL + cid + ".xml";

        try {
            // 获取弹幕的原始XML数据
            String response = HttpClientUtil.sendGetRequestWithDeflate(url, cookie);

            // 解析XML并返回弹幕列表
            return parseBarrageXml(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析弹幕XML数据并返回弹幕列表
     *
     * @param xmlData 弹幕的XML数据
     * @return List<Barrage> 弹幕列表
     */
    private static List<Barrage> parseBarrageXml(String xmlData) {
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
                float time = Float.parseFloat(pAttributes[0]);
                int type = Integer.parseInt(pAttributes[1]);
                int fontSize = Integer.parseInt(pAttributes[2]);
                int color = Integer.parseInt(pAttributes[3]);
                long sendTime = Long.parseLong(pAttributes[4]);
                int poolType = Integer.parseInt(pAttributes[5]);
                String senderHash = pAttributes[6];
                long dmid = Long.parseLong(pAttributes[7]);
                int shieldLevel = pAttributes.length > 8 ? Integer.parseInt(pAttributes[8]) : 0;

                // 获取弹幕内容
                String content = element.getTextContent();

                // 创建Barrage对象并加入列表
                Barrage barrage = new Barrage(time, type, fontSize, color, sendTime, poolType, senderHash, dmid, shieldLevel, content);
                barrageList.add(barrage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return barrageList;
    }
}