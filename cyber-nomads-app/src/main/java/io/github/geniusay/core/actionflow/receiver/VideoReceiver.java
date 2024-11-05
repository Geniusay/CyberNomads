package io.github.geniusay.core.actionflow.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import lombok.Data;

@Data
public class VideoReceiver implements Receiver {

    private final String id;
    private final String title;

    public VideoReceiver(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "视频";
    }
}