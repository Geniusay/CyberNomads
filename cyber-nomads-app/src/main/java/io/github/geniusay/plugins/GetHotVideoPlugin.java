package io.github.geniusay.plugins;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.geniusay.constants.VideoCacheConstants;
import io.github.geniusay.core.supertask.plugin.comment.GetHandleVideo;
import io.github.geniusay.crawler.api.bilibili.BilibiliHotApi;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.VideoCacheConstants.DEFAULT_CACHE_DURATION_MINUTES;
import static java.util.concurrent.TimeUnit.MINUTES;

@Component
public class GetHotVideoPlugin implements GetHandleVideo<VideoDetail> {

    // Caffeine缓存实例
    private Cache<String, List<VideoDetail>> hotRankingVideosCache;
    private Cache<String, List<VideoDetail>> popularVideosCache;

    /**
     * 初始化缓存
     * 这里的缓存时间是程序内部指定的，前端无法控制。
     */
    @PostConstruct
    public void init() {
        hotRankingVideosCache = createCache();
        popularVideosCache = createCache();
    }

    /**
     * 创建 Caffeine 缓存
     *
     * @return Cache 实例
     */
    private Cache<String, List<VideoDetail>> createCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(DEFAULT_CACHE_DURATION_MINUTES, MINUTES)
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
     *               - "tid" (Integer, 可选): 当 "videoType" 为 "ranking" 时，指定分区ID（tid），如果不传则获取所有分区的排行榜。
     *               - "pn" (Integer, 可选): 当 "videoType" 为 "popular" 时，指定页码，默认为 1。
     *               - "ps" (Integer, 可选): 当 "videoType" 为 "popular" 时，指定每页项数，默认为 20。
     * @return 视频列表
     */
    @Override
    public List<VideoDetail> getHandleVideo(Map<String, Object> params) {
        // 使用 ParamsHelper 提取并验证参数
        Map<String, Object> validatedParams = getParams(params);

        // 判断获取哪种视频列表，默认是热门视频列表
        String videoType = getValue(validatedParams, "videoType", String.class);

        if ("ranking".equalsIgnoreCase(videoType)) {
            return hotRankingVideosCache.get(VideoCacheConstants.HOT_RANKING_VIDEOS_CACHE_KEY, key -> fetchHotRankingVideos(validatedParams));
        } else {
            return popularVideosCache.get(VideoCacheConstants.POPULAR_VIDEOS_CACHE_KEY, key -> fetchPopularVideos(validatedParams));
        }
    }

    /**
     * 调用 BilibiliHotApi 获取排行榜前100的视频
     */
    private List<VideoDetail> fetchHotRankingVideos(Map<String, Object> params) {
        Integer tid = getValue(params, "tid", Integer.class);
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
        int pageNumber = getValue(params, "pn", Integer.class);
        int pageSize = getValue(params, "ps", Integer.class);
        ApiResponse<List<VideoDetail>> response = BilibiliHotApi.getPopularVideos(pageNumber, pageSize);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new RuntimeException("获取B站热门视频失败: " + response.getMsg());
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("videoType", String.class, "指定要获取的视频类型", false, "popular"),
                new TaskNeedParams("tid", Integer.class, "分区ID", false, null),
                new TaskNeedParams("pn", Integer.class, "页码", false, VideoCacheConstants.DEFAULT_PAGE_NUMBER),
                new TaskNeedParams("ps", Integer.class, "每页项数", false, VideoCacheConstants.DEFAULT_PAGE_SIZE)
        );
    }
}