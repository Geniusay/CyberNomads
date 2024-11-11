package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

public class BiliLikeLogic extends ActionLogic<BiliUserActor, BiliVideoReceiver> {

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliVideoReceiver receiver) throws Exception {
        String cookie = actor.getCookie();
        String videoId = receiver.getId();  // 获取视频的 bvid 或 aid

        logAction(actor, receiver, "点赞成功");

        // 调用 Bilibili API 进行点赞操作
        return BilibiliVideoApi.likeVideo(cookie, videoId, 1);  // 1 表示点赞
    }
}