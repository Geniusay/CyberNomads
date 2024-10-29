package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliHotApi;
import io.github.geniusay.crawler.po.bilibili.SeriesDetail;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.po.bilibili.WeeklySeries;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;

import java.util.List;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestHotApi {

    @Test
    public void getRank100() {
        ApiResponse<List<VideoDetail>> hotRankingVideos = BilibiliHotApi.getHotRankingVideos(1);
        if (hotRankingVideos.isSuccess()) {
            List<VideoDetail> hotRankingVideoList = hotRankingVideos.getData();
            for (VideoDetail videoDetail : hotRankingVideoList) {
                System.out.println(videoDetail);
            }
        }
    }

    @Test
    public void getHot() {
        ApiResponse<List<VideoDetail>> hotRankingVideos = BilibiliHotApi.getPopularVideos(1, 20);
        if (hotRankingVideos.isSuccess()) {
            List<VideoDetail> hotRankingVideoList = hotRankingVideos.getData();
            for (VideoDetail videoDetail : hotRankingVideoList) {
                System.out.println(videoDetail);
            }
        }
    }

    @Test
    public void getWeeklySeries() {
        ApiResponse<List<WeeklySeries>> response = BilibiliHotApi.getWeeklySeriesList();
        if (response.isSuccess()) {
            List<WeeklySeries> weeklySeries = response.getData();
            for (WeeklySeries series : weeklySeries) {
                System.out.println(series);
            }
        }
    }

    @Test
    public void getWeeklyDetail() {
        // 获取第3期的详细信息
        ApiResponse<SeriesDetail> response = BilibiliHotApi.getSeriesDetail(cookie, 292);

        if (response.isSuccess()) {
            SeriesDetail seriesDetail = response.getData();
            System.out.println("选期名称: " + seriesDetail.getConfig().getName());
            System.out.println("选期主题: " + seriesDetail.getConfig().getSubject());
            System.out.println("选期状态: " + (seriesDetail.getConfig().getStatus() == 2 ? "已结束" : "进行中"));
            System.out.println("选期视频列表:");
            for (VideoDetail video : seriesDetail.getList()) {
                System.out.println("  视频标题: " + video.getData().getTitle());
                System.out.println("  播放量: " + video.getData().getStat().getView());
                System.out.println("  UP主: " + video.getData().getOwner().getName());
                System.out.println("  ---------");
            }
        }
    }
}
