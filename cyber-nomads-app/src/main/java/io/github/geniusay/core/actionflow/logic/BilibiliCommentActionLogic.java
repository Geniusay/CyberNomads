package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliCommentReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

public class BilibiliCommentActionLogic extends ActionLogic<BilibiliUserActor, BilibiliCommentReceiver> {

    private final String commentStr;
    private final boolean isTestMode;

    public BilibiliCommentActionLogic(String commentStr, boolean isTestMode) {
        this.commentStr = commentStr;
        this.isTestMode = isTestMode;
    }

    @Override
    public ApiResponse<Boolean> performAction(BilibiliUserActor actor, BilibiliCommentReceiver receiver) throws Exception {
        if (isTestMode) {
            logAction(actor, receiver, "【测试模式】发表评论/回复: " + commentStr);
            return new ApiResponse<>(0, "测试成功", true, true, System.currentTimeMillis(), System.currentTimeMillis(), 0);
        }

        String cookie = actor.getCookie();
        String oid = receiver.getId();
        String rpid = receiver.getRpid();
        String parentRpid = receiver.getParentRpid();

        logAction(actor, receiver, "发表评论/回复: " + commentStr);

        return BilibiliCommentApi.sendCommentOrReply(cookie, oid, commentStr, rpid, parentRpid);
    }
}