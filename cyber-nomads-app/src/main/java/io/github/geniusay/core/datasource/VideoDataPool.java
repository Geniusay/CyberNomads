package io.github.geniusay.core.datasource;

import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;

import java.util.List;

public interface VideoDataPool {

    /**
     * 获取排行榜前100的视频
     */
    List<BilibiliVideoDetail> getRankingVideos();

    /**
     * 获取热门视频列表
     */
    List<BilibiliVideoDetail> getPopularVideos();

    /**
     * 手动触发更新排行榜视频
     */
    void updateRankingVideos();

    /**
     * 手动触发更新热门视频
     */
    void updatePopularVideos();
}