package io.github.geniusay.core.actionflow.logic;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiliCoinLogic extends ActionLogic<BiliUserActor, BiliVideoReceiver> {

    private final boolean isTestMode;  // 是否为测试模式

    private Integer coinSum;

    private Integer andLike;

    public BiliCoinLogic(Integer coinSum, boolean andLike, boolean isTestMode) {
        this.coinSum = coinSum;
        if (andLike) {
            this.andLike = 1;
        } else {
            this.andLike = 0;
        }
        this.isTestMode = isTestMode;
    }

    @Override
    public ApiResponse<Boolean> performAction(BiliUserActor actor, BiliVideoReceiver receiver) throws Exception {
        if (isTestMode) {
            logAction(actor, receiver, "【测试模式】执行投币操作，投币数量：" + coinSum + "是否同时点赞：" + andLike);
            return new ApiResponse<>(0, "测试成功", true, true, System.currentTimeMillis(), System.currentTimeMillis(), 0);
        }

        String cookie = actor.getCookie();
        String videoId = receiver.getId();
        logAction(actor, receiver, "执行投币操作");
        return BilibiliVideoApi.coinVideo(cookie, videoId, coinSum, andLike);
    }
}