package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiliTripletLogic extends ActionLogic<BiliUserActor, BiliVideoReceiver> {

    @Override
    public String getLogicName() {
        return "三连操作";
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliVideoReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String videoId = receiver.getId();
        logAction(actor, receiver, "执行三连操作");
        return BilibiliVideoApi.tripleAction(cookie, videoId);
    }
}