package io.github.geniusay.crawler.po.bilibili;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Barrage {

    private long dmid;       // 弹幕的唯一ID
    private String content;  // 弹幕内容
    private float time;      // 视频内弹幕出现时间（秒）
    private long sendTime;   // 弹幕发送时间，时间戳
    private int type;        // 弹幕类型
    private String senderHash; // 发送者mid的HASH

    /**
     * 从 p 属性和弹幕内容创建 Barrage 实例
     *
     * @param pAttributes p 属性数组，已按逗号分割
     * @param content     弹幕内容
     * @return Barrage 实例
     */
    public static Barrage fromAttributes(String[] pAttributes, String content) {
        float time = Float.parseFloat(pAttributes[0]);         // 视频内弹幕出现时间
        int type = Integer.parseInt(pAttributes[1]);           // 弹幕类型
        long sendTime = Long.parseLong(pAttributes[4]);        // 弹幕发送时间
        String senderHash = pAttributes[6];                    // 发送者mid的HASH
        long dmid = Long.parseLong(pAttributes[7]);            // 弹幕的唯一ID

        // 创建并返回Barrage对象
        return new Barrage(dmid, content, time, sendTime, type, senderHash);
    }
}