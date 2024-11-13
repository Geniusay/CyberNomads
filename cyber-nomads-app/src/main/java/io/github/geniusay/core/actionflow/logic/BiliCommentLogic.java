package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliCommentReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

public class BiliCommentLogic extends ActionLogic<BiliUserActor, BiliCommentReceiver> {

    private String content;

    public BiliCommentLogic(String content) {
        this.content = content;
    }

    @Override
    public String getLogicName() {
        return "评论操作";
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliCommentReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String oid = receiver.getId();
        String rpid = receiver.getRpid();
        String parentRpid = receiver.getParentRpid();

        logAction(actor, receiver, "发表评论/回复: " + content);

        return BilibiliCommentApi.sendCommentOrReply(cookie, oid, content, rpid, parentRpid);
    }

    public void setContent(String content) {
        this.content = content;
    }
}