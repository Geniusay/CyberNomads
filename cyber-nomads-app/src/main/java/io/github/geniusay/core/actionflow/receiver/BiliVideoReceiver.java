package io.github.geniusay.core.actionflow.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import lombok.Data;

@Data
public class BiliVideoReceiver implements Receiver {

    private final String videoId;  // 视频的 bvid 或 aid

    public BiliVideoReceiver(String videoId) {
        this.videoId = videoId;
    }

    public BiliVideoReceiver(String videoId, String videoTitle) {
        this.videoId = videoId;
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