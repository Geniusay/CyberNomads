package io.github.geniusay.core.actionflow.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class BiliUserReceiver implements Receiver {

    private final String uid;

    public BiliUserReceiver(String uid) {
        this.uid = uid;
    }

    @Override
    public String getId() {
        return uid;
    }

    @Override
    public String getType() {
        return "用户";
    }
}