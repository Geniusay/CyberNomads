package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliCommentReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

public class BiliTestLogic extends ActionLogic<BiliUserActor, BiliCommentReceiver> {

    private final String commentStr;

    public BiliTestLogic(String commentStr) {
        this.commentStr = commentStr;
    }

    @Override
    public String getLogicName() {
        return "测试操作";
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliCommentReceiver receiver) throws Exception {
        logAction(actor, receiver, "【测试模式】发表评论/回复: " + commentStr);
        return new ApiResponse<>(0, "测试成功", true, true, System.currentTimeMillis(), System.currentTimeMillis(), 0);
    }
}