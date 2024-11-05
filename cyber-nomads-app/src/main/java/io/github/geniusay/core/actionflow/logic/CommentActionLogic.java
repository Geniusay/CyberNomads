package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.UserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;

import io.github.geniusay.core.actionflow.receiver.VideoReceiver;

public class CommentActionLogic extends ActionLogic<UserActor, VideoReceiver> {

    private final String comment;

    public CommentActionLogic(String comment) {
        this.comment = comment;
    }

    @Override
    public void performAction(UserActor actor, VideoReceiver receiver) throws Exception {
        System.out.printf("%s 对 %s 发表评论: %s%n", actor.getName(), receiver.getTitle(), comment);
    }
}