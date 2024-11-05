package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.UserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.VideoReceiver;

public class LikeActionLogic extends ActionLogic<UserActor, VideoReceiver> {

    @Override
    public void performAction(UserActor actor, VideoReceiver receiver) throws Exception {
        System.out.printf("%s 给 %s 点赞%n", actor.getName(), receiver.getId());
//        logAction(actor, receiver, "点赞");
    }
}