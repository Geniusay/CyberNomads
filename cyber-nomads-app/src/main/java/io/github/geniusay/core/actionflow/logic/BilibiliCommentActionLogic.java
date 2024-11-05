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
    public void performAction(BilibiliUserActor actor, BilibiliCommentReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String oid = receiver.getId();  // 获取视频的评论区ID (oid)
        String rpid = receiver.getRpid();  // 获取根评论的rpid
        String parentRpid = receiver.getParentRpid();  // 获取父评论的rpid

        // 调用 Bilibili API 发送评论或回复
        ApiResponse<Boolean> response = BilibiliCommentApi.sendCommentOrReply(cookie, oid, commentStr, rpid, parentRpid);

        // 检查评论或回复是否成功
        if (!response.isSuccess()) {
            throw new RuntimeException(String.format("评论失败: 视频 %s (oid: %s), 错误信息: %s", oid, rpid, response.getMsg()));
        }

        // 记录日志
        logAction(actor, receiver, "发表评论/回复: " + commentStr);
    }
}