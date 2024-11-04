package io.github.geniusay.plugins;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.geniusay.constants.VideoCacheConstants;
import io.github.geniusay.core.supertask.plugin.comment.GetHandleVideo;
import io.github.geniusay.crawler.api.bilibili.BilibiliHotApi;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class GetHotVideoPlugin implements GetHandleVideo<VideoDetail> {

    // Caffeine缓存实例
    private Cache<String, List<VideoDetail>> hotRankingVideosCache;
    private Cache<String, List<VideoDetail>> popularVideosCache;

    /**
     * 初始化缓存
     */
    @PostConstruct
    public void init() {
        hotRankingVideosCache = Caffeine.newBuilder()
                .expireAfterWrite(VideoCacheConstants.DEFAULT_CACHE_DURATION_MINUTES, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        popularVideosCache = Caffeine.newBuilder()
                .expireAfterWrite(VideoCacheConstants.DEFAULT_CACHE_DURATION_MINUTES, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    /**
     * 获取视频列表，并使用缓存
     *
     * @param params 参数列表，支持以下参数：
     *               - "videoType" (String): 指定要获取的视频类型。可选值为 "ranking" 或 "popular"。
     *                 - "ranking": 获取排行榜前100的视频。
     *                 - "popular" (默认): 获取当前热门视频列表。
     *               - "cacheDuration" (Long): 指定缓存的有效时间，单位为分钟。默认为 10 分钟。
     *               - "tid" (Integer, 可选): 当 "videoType" 为 "ranking" 时，指定分区ID（tid），如果不传则获取所有分区的排行榜。
     *               - "pn" (Integer, 可选): 当 "videoType" 为 "popular" 时，指定页码，默认为 1。
     *               - "ps" (Integer, 可选): 当 "videoType" 为 "popular" 时，指定每页项数，默认为 20。
     * @return 视频列表
     */
    @Override
    public List<VideoDetail> getHandleVideo(Map<String, Object> params) {
        // 获取缓存时间，默认为10分钟
        long cacheDuration = params.containsKey("cacheDuration") ?
                (long) params.get("cacheDuration") : VideoCacheConstants.DEFAULT_CACHE_DURATION_MINUTES;

        // 如果缓存时间不同于默认值，重新设置缓存时间
        if (cacheDuration != VideoCacheConstants.DEFAULT_CACHE_DURATION_MINUTES) {
            hotRankingVideosCache = Caffeine.newBuilder()
                    .expireAfterWrite(cacheDuration, TimeUnit.MINUTES)
                    .maximumSize(100)
                    .build();

            popularVideosCache = Caffeine.newBuilder()
                    .expireAfterWrite(cacheDuration, TimeUnit.MINUTES)
                    .maximumSize(100)
                    .build();
        }

        // 判断获取哪种视频列表，默认是热门视频列表
        String videoType = (String) params.getOrDefault("videoType", "popular");

        if ("ranking".equalsIgnoreCase(videoType)) {
            return hotRankingVideosCache.get(VideoCacheConstants.HOT_RANKING_VIDEOS_CACHE_KEY, key -> fetchHotRankingVideos(params));
        } else {
            return popularVideosCache.get(VideoCacheConstants.POPULAR_VIDEOS_CACHE_KEY, key -> fetchPopularVideos(params));
        }
    }

    /**
     * 调用 BilibiliHotApi 获取排行榜前100的视频
     */
    private List<VideoDetail> fetchHotRankingVideos(Map<String, Object> params) {
        Integer tid = params.containsKey("tid") ? (Integer) params.get("tid") : null;
        ApiResponse<List<VideoDetail>> response = BilibiliHotApi.getHotRankingVideos(tid);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new RuntimeException("获取B站排行榜视频失败: " + response.getMsg());
        }
    }

    /**
     * 调用 BilibiliHotApi 获取当前热门视频列表
     */
    private List<VideoDetail> fetchPopularVideos(Map<String, Object> params) {
        int pageNumber = params.containsKey("pn") ? (int) params.get("pn") : VideoCacheConstants.DEFAULT_PAGE_NUMBER;
        int pageSize = params.containsKey("ps") ? (int) params.get("ps") : VideoCacheConstants.DEFAULT_PAGE_SIZE;
        ApiResponse<List<VideoDetail>> response = BilibiliHotApi.getPopularVideos(pageNumber, pageSize);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new RuntimeException("获取B站热门视频失败: " + response.getMsg());
        }
    }
}