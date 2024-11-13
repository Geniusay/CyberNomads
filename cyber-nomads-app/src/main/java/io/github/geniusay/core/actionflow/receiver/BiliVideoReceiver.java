package io.github.geniusay.core.actionflow.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import lombok.Data;

@Data
public class BiliVideoReceiver implements Receiver {

    private final String bvid;  // 视频的 BV 号

    public BiliVideoReceiver(String bvid) {
        this.bvid = bvid;
    }

    @Override
    public String getId() {
        return bvid;
    }

    @Override
    public String getType() {
        return "视频";
    }
}