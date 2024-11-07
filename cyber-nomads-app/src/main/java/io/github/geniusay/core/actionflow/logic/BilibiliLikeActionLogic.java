package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliVideoReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

public class BilibiliLikeActionLogic extends ActionLogic<BilibiliUserActor, BilibiliVideoReceiver> {

    @Override
    public ApiResponse<Boolean> performAction(BilibiliUserActor actor, BilibiliVideoReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String videoId = receiver.getId();  // 获取视频的 bvid 或 aid

        logAction(actor, receiver, "点赞成功");

        // 调用 Bilibili API 进行点赞操作
        return BilibiliVideoApi.likeVideo(cookie, videoId, 1);  // 1 表示点赞
    }
}