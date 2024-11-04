package io.github.geniusay.plugins;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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
    private Cache<String, List<VideoDetail>> videoCache;

    private static final long DEFAULT_CACHE_DURATION_MINUTES = 10;

    /**
     * 初始化缓存
     */
    @PostConstruct
    public void init() {
        // 初始化缓存，默认10分钟过期
        videoCache = Caffeine.newBuilder()
                .expireAfterWrite(DEFAULT_CACHE_DURATION_MINUTES, TimeUnit.MINUTES)
                .maximumSize(100)  // 设置最大缓存条目数
                .build();
    }

    /**
     * 获取热门视频列表，并使用缓存
     *
     * @param params 参数列表，支持传入缓存时间
     * @return 热门视频列表
     */
    @Override
    public List<VideoDetail> getHandleVideo(Map<String, Object> params) {
        // 获取缓存时间，默认为10分钟
        long cacheDuration = params.containsKey("cacheDuration") ?
                (long) params.get("cacheDuration") : DEFAULT_CACHE_DURATION_MINUTES;

        // 如果缓存时间不同于默认值，重新设置缓存时间
        if (cacheDuration != DEFAULT_CACHE_DURATION_MINUTES) {
            videoCache = Caffeine.newBuilder()
                    .expireAfterWrite(cacheDuration, TimeUnit.MINUTES)
                    .maximumSize(100)
                    .build();
        }

        // 从缓存中获取热门视频列表，如果缓存不存在则调用API获取
        return videoCache.get("hotVideos", key -> fetchHotVideos());
    }

    /**
     * 调用 BilibiliHotApi 获取热门视频列表
     *
     * @return 热门视频列表
     */
    private List<VideoDetail> fetchHotVideos() {
        ApiResponse<List<VideoDetail>> response = BilibiliHotApi.getPopularVideos(1, 20);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new RuntimeException("获取B站热门视频失败: " + response.getMsg());
        }
    }
}