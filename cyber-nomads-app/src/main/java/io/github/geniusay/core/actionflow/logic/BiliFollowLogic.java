package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliUserReceiver;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiliFollowLogic extends ActionLogic<BiliUserActor, BiliUserReceiver> {

    public BiliFollowLogic() {
    }

    @Override
    public String getLogicName() {
        return "关注操作";
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliUserReceiver receiver) throws Exception {
        return null;
    }
}