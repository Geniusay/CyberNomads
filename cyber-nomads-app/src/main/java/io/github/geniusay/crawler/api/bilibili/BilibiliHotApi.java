package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliHotHandler;
import io.github.geniusay.crawler.po.bilibili.SeriesDetail;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.po.bilibili.WeeklySeries;
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

    /**
     * 获取当前热门视频列表
     *
     * @param pn 页码，默认为1
     * @param ps 每页项数，默认为20
     * @return ApiResponse<List<VideoDetail>> 当前热门视频列表
     */
    public static ApiResponse<List<VideoDetail>> getPopularVideos(int pn, int ps) {
        return BilibiliHotHandler.getPopularVideos(pn, ps);
    }

    /**
     * 获取每周必看全部列表
     *
     * @return ApiResponse<List<WeeklySeries>> 每周必看全部列表
     */
    public static ApiResponse<List<WeeklySeries>> getWeeklySeriesList() {
        return BilibiliHotHandler.getWeeklySeriesList();
    }

    /**
     * 获取每周必看选期详细信息
     *
     * @param number 期数
     * @return ApiResponse<SeriesDetail> 选期详细信息
     */
    public static ApiResponse<SeriesDetail> getSeriesDetail(String cookie, int number) {
        return BilibiliHotHandler.getSeriesDetail(cookie, number);
    }
}
