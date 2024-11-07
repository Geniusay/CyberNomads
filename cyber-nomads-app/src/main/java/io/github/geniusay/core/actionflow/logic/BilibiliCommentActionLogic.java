package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliCommentReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

public class BilibiliCommentActionLogic extends ActionLogic<BilibiliUserActor, BilibiliCommentReceiver> {

    private final String commentStr;

    public BilibiliCommentActionLogic(String commentStr) {
        this.commentStr = commentStr;
    }

    @Override
    public ApiResponse<Boolean> performAction(BilibiliUserActor actor, BilibiliCommentReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String oid = receiver.getId();  // 获取视频的评论区ID (oid)
        String rpid = receiver.getRpid();  // 获取根评论的rpid
        String parentRpid = receiver.getParentRpid();  // 获取父评论的rpid

        logAction(actor, receiver, "发表评论/回复: " + commentStr);

        return BilibiliCommentApi.sendCommentOrReply(cookie, oid, commentStr, rpid, parentRpid);
    }
}