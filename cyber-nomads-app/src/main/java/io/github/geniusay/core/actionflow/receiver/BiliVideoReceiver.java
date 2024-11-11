package io.github.geniusay.core.actionflow.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class BiliVideoReceiver implements Receiver {

    private final String videoId;  // 视频的 BV 号

    // 正则表达式匹配 BV 号
    private static final Pattern BV_PATTERN = Pattern.compile("BV([a-zA-Z0-9]{10})");

    /**
     * 构造方法，支持传入视频链接或 BV 号
     */
    public BiliVideoReceiver(String videoLinkOrId) {
        this.videoId = extractBvId(videoLinkOrId);
    }

    /**
     * 从视频链接或 BV 号中提取 BV 号
     */
    private String extractBvId(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("输入的视频链接或 BV 号不能为空");
        }

        // 尝试匹配 BV 号
        Matcher matcher = BV_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group(0);  // 返回完整的 BV 号
        }

        throw new IllegalArgumentException("无效的视频链接或 BV 号: " + input);
    }

    @Override
    public String getId() {
        return videoId;
    }

    @Override
    public String getType() {
        return "视频";
    }
}