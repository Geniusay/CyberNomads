package io.github.geniusay.core.actionflow.actor;

import io.github.geniusay.core.actionflow.frame.Actor;
import io.github.geniusay.core.supertask.task.RobotWorker;
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

    public BilibiliUserActor(RobotWorker robot) {
        this.id = robot.getId().toString();
        this.nickname = robot.getUsername();
        this.cookie = robot.getCookie();
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