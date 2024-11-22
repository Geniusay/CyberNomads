package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliSearchApi;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.VideoAiSummaryData;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoSearchResult;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;

import java.util.List;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestSearchAPI {

    public static String imgKey = "7cd084941338484aae1ad9425b84077c";
    public static String subKey = "4932caff0ff746eab6f01bf08b70ac45";

    @Test
    public void getVideoInfo() throws Exception {
        String keyword = "java"; // 搜索关键词
        String order = "click"; // 排序方式
        int duration = 0; // 视频时长筛选
        int tids = 0; // 视频分区筛选
        int page = 1; // 页码
        int pageSize = 42; // 页码

        ApiResponse<VideoSearchResult> response = BilibiliSearchApi.searchVideos(keyword, order, duration, tids, page, pageSize, imgKey, subKey);

        if (response.isSuccess()) {
            VideoSearchResult result = response.getData();
            System.out.println("搜索成功，当前页码: " + result.getData().getPage());
            for (VideoSearchResult.Data.Result video : result.getData().getResult()) {
                System.out.println("标题: " + video.getTitle());
                System.out.println("链接: " + video.getArcurl());
                System.out.println("---------------------------");
            }
        } else {
            System.err.println("搜索失败: " + response.getMsg());
        }
    }

}
