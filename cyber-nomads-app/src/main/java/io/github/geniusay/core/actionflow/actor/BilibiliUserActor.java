package io.github.geniusay.core.actionflow.actor;

import io.github.geniusay.core.actionflow.frame.Actor;
import lombok.Data;

@Data
public class BilibiliUserActor implements Actor {

    private final String id;
    private final String nickname;
    private final String cookie;

    public BilibiliUserActor(String id, String nickname, String cookie) {
        this.id = id;
        this.nickname = nickname;
        this.cookie = cookie;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return nickname;
    }
}