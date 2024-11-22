package io.github.geniusay.crawler.api.bilibili;

import io.github.geniusay.crawler.handler.bilibili.BilibiliSearchHandler;
import io.github.geniusay.crawler.po.bilibili.HotSearchResult;
import io.github.geniusay.crawler.po.bilibili.VideoSearchResult;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.crawler.util.bilibili.HttpClientUtil;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.Objects;

/**
 * 描述: B站搜索相关API
 * @author suifeng
 * 日期: 2024/10/28
 */
public class BilibiliSearchApi {

    /**
     * 按关键词搜索视频
     *
     * @param keyword  搜索关键词
     * @param order    排序方式 (如: totalrank：综合排序, click：最多点击, pubdate：最新发布)
     * @param duration 视频时长筛选 (0: 全部, 1: 10分钟以下, 2: 10-30分钟, 3: 30-60分钟, 4: 60分钟以上)
     * @param tids     视频分区筛选 (0: 全部分区, 具体分区ID参考文档)
     * @param page     页码
     * @return ApiResponse<VideoSearchResult> 包含搜索结果的响应对象
     */
    public static ApiResponse<VideoSearchResult> searchVideos(String keyword, String order, int duration, int tids, int page, int page_size, String imgKey, String subKey) {
        return BilibiliSearchHandler.searchVideos(keyword, order, duration, tids, page, page_size, imgKey, subKey);
    }


    /**
     * 获取 B站热搜列表
     *
     * @return ApiResponse<HotSearchResult> 包含热搜列表的响应对象
     */
    public static ApiResponse<HotSearchResult> getHotSearchList() {
        return BilibiliSearchHandler.getHotSearchList();
    }
}