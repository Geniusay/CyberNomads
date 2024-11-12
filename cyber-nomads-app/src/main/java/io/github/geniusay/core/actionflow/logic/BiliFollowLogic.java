package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliUserReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliUserApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiliFollowLogic extends ActionLogic<BiliUserActor, BiliUserReceiver> {

    private Integer reSrc;

    public BiliFollowLogic(Integer reSrc) {
        this.reSrc = reSrc;
    }

    @Override
    public String getLogicName() {
        return "关注操作";
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliUserReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String videoId = receiver.getId();
        logAction(actor, receiver, "执行关注操作");
        return BilibiliUserApi.followUser(cookie, receiver.getId(), reSrc);
    }
}