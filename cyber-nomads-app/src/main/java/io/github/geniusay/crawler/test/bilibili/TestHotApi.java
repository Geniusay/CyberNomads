package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliHotApi;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;

import java.util.List;

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
}
