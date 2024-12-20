package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiliLikeLogic extends ActionLogic<BiliUserActor, BiliVideoReceiver> {

    @Override
    public String getLogicName() {
        return "点赞操作";
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliVideoReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String videoId = receiver.getId();
        logAction(actor, receiver, "执行点赞操作");
        return BilibiliVideoApi.likeVideo(cookie, videoId, 1);
    }
}