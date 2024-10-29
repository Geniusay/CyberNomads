package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliHotHandler;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;

import java.util.List;

/**
 * 描述: B站热门视频/排行榜相关API
 * @author suifeng
 * 日期: 2024/10/29
 */
public class BilibiliHotApi {

    /**
     * 获取B站视频排行榜前100的视频
     *
     * @param tid 分区ID，非必要参数
     * @return ApiResponse<List<VideoDetail.Data>> 前100个视频的详细信息
     */
    public static ApiResponse<List<VideoDetail>> getHotRankingVideos(Integer tid) {
        return BilibiliHotHandler.getHotRankingVideos(tid);
    }
}
