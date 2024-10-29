package io.github.geniusay.crawler.po.bilibili;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Barrage {

    private float time;      // 视频内弹幕出现时间（秒）
    private int type;        // 弹幕类型
    private int fontSize;    // 弹幕字号
    private int color;       // 弹幕颜色，十进制RGB888值
    private long sendTime;   // 弹幕发送时间，时间戳
    private int poolType;    // 弹幕池类型
    private String senderHash; // 发送者mid的HASH
    private long dmid;       // 弹幕的唯一ID
    private int shieldLevel; // 弹幕的屏蔽等级
    private String content;  // 弹幕内容
}