package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliCommentReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

public class BiliCommentLogic extends ActionLogic<BiliUserActor, BiliCommentReceiver> {

    private final String commentStr;

    public BiliCommentLogic(String commentStr) {
        this.commentStr = commentStr;
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliCommentReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String oid = receiver.getId();
        String rpid = receiver.getRpid();
        String parentRpid = receiver.getParentRpid();

        logAction(actor, receiver, "发表评论/回复: " + commentStr);

        return BilibiliCommentApi.sendCommentOrReply(cookie, oid, commentStr, rpid, parentRpid);
    }
}