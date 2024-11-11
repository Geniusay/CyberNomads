package io.github.geniusay.core.actionflow.actor;

import io.github.geniusay.core.actionflow.frame.Actor;
import io.github.geniusay.core.supertask.task.RobotWorker;
import lombok.Data;

@Data
public class BiliUserActor implements Actor {

    private final String id;
    private final String nickname;
    private final String cookie;

    public BiliUserActor(String id, String nickname, String cookie) {
        this.id = id;
        this.nickname = nickname;
        this.cookie = cookie;
    }

    public BiliUserActor(RobotWorker robot) {
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