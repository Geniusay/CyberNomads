package io.github.geniusay.core.datasource;

import io.github.geniusay.crawler.api.bilibili.BilibiliHotApi;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BilibiliVideoDataPool implements VideoDataPool {

    // Redis key 定义
    private static final String RANKING_VIDEOS_KEY = "bilibili:rankingVideos";
    private static final String POPULAR_VIDEOS_KEY = "bilibili:popularVideos";

    // 常量定义
    private static final long CACHE_EXPIRE_TIME = 3600;  // 缓存过期时间，单位秒，1小时
    private static final int POPULAR_PAGES = 5;          // 爬取热门视频的页数
    private static final int PAGE_SIZE = 20;             // 每页爬取的视频数量
    private static final int MAX_VIDEO_COUNT = 100;      // 最大视频数量

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取排行榜前100的视频
     */
    @Override
    public List<BilibiliVideoDetail> getRankingVideos() {
        // 从 Redis 获取缓存
        List<String> rankingVideosJson = stringRedisTemplate.opsForList().range(RANKING_VIDEOS_KEY, 0, -1);

        if (rankingVideosJson == null || rankingVideosJson.isEmpty()) {
            // 如果缓存不存在，手动更新并返回
            log.info("排行榜视频缓存不存在，正在从B站获取...");
            updateRankingVideos();
            rankingVideosJson = stringRedisTemplate.opsForList().range(RANKING_VIDEOS_KEY, 0, -1);
        }

        // 将 JSON 字符串转换为 BilibiliVideoDetail 对象列表
        return rankingVideosJson.stream()
                .map(BilibiliVideoDetail::fromJson)
                .collect(Collectors.toList());
    }

    /**
     * 获取热门视频列表
     */
    @Override
    public List<BilibiliVideoDetail> getPopularVideos() {
        // 从 Redis 获取缓存
        List<String> popularVideosJson = stringRedisTemplate.opsForList().range(POPULAR_VIDEOS_KEY, 0, -1);

        if (popularVideosJson == null || popularVideosJson.isEmpty()) {
            // 如果缓存不存在，手动更新并返回
            log.info("热门视频缓存不存在，正在从B站获取...");
            updatePopularVideos();
            popularVideosJson = stringRedisTemplate.opsForList().range(POPULAR_VIDEOS_KEY, 0, -1);
        }

        // 将 JSON 字符串转换为 BilibiliVideoDetail 对象列表
        return popularVideosJson.stream()
                .map(BilibiliVideoDetail::fromJson)
                .collect(Collectors.toList());
    }

    /**
     * 手动触发更新排行榜视频
     */
    @Override
    public void updateRankingVideos() {
        try {
            ApiResponse<List<VideoDetail>> response = BilibiliHotApi.getHotRankingVideos(null);
            if (response.isSuccess()) {
                List<BilibiliVideoDetail> rankingVideos = response.getData().stream()
                        .map(BilibiliVideoDetail::fromVideoDetail)
                        .collect(Collectors.toList());

                // 清空 Redis 中的旧数据
                stringRedisTemplate.delete(RANKING_VIDEOS_KEY);

                // 批量插入新数据到 Redis
                List<String> rankingVideosJson = rankingVideos.stream()
                        .map(BilibiliVideoDetail::toJson)
                        .collect(Collectors.toList());

                stringRedisTemplate.opsForList().leftPushAll(RANKING_VIDEOS_KEY, rankingVideosJson);

                // 保持列表长度为 100
                stringRedisTemplate.opsForList().trim(RANKING_VIDEOS_KEY, 0, MAX_VIDEO_COUNT - 1);

                // 设置过期时间
                stringRedisTemplate.expire(RANKING_VIDEOS_KEY, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);

                log.info("成功更新排行榜视频缓存");
            } else {
                log.error("获取排行榜视频失败: {}", response.getMsg());
            }
        } catch (Exception e) {
            log.error("更新排行榜视频缓存时发生错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 手动触发更新热门视频
     */
    @Override
    public void updatePopularVideos() {
        try {
            List<BilibiliVideoDetail> allPopularVideos = new ArrayList<>();
            // 爬取指定页数的热门视频
            for (int i = 1; i <= POPULAR_PAGES; i++) {
                ApiResponse<List<VideoDetail>> response = BilibiliHotApi.getPopularVideos(i, PAGE_SIZE);
                if (response.isSuccess()) {
                    allPopularVideos.addAll(response.getData().stream()
                            .map(BilibiliVideoDetail::fromVideoDetail)
                            .collect(Collectors.toList()));
                } else {
                    log.error("获取热门视频失败: {}", response.getMsg());
                    break;
                }
            }

            // 清空 Redis 中的旧数据
            stringRedisTemplate.delete(POPULAR_VIDEOS_KEY);

            // 批量插入新数据到 Redis
            List<String> popularVideosJson = allPopularVideos.stream()
                    .map(BilibiliVideoDetail::toJson)
                    .collect(Collectors.toList());

            stringRedisTemplate.opsForList().leftPushAll(POPULAR_VIDEOS_KEY, popularVideosJson);

            // 保持列表长度为 100
            stringRedisTemplate.opsForList().trim(POPULAR_VIDEOS_KEY, 0, MAX_VIDEO_COUNT - 1);

            // 设置过期时间
            stringRedisTemplate.expire(POPULAR_VIDEOS_KEY, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);

            log.info("成功更新热门视频缓存");
        } catch (Exception e) {
            log.error("更新热门视频缓存时发生错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 定时任务：每隔一段时间更新排行榜视频缓存
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void scheduledUpdateRankingVideos() {
        log.info("定时更新排行榜视频缓存...");
        updateRankingVideos();
    }

    /**
     * 定时任务：每隔一段时间更新热门视频缓存
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scheduledUpdatePopularVideos() {
        log.info("定时更新热门视频缓存...");
        updatePopularVideos();
    }
}