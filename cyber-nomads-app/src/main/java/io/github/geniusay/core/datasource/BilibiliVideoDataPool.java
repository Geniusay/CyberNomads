package io.github.geniusay.core.datasource;

import io.github.geniusay.crawler.api.bilibili.BilibiliHotApi;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.github.geniusay.constants.RedisConstant.*;

@Slf4j
@Component
public class BilibiliVideoDataPool implements VideoDataPool {

    private static final long CACHE_EXPIRE_TIME = 1800;  // 排行榜缓存过期时间，单位秒，30分钟
    private static final int MAX_RANKING_VIDEO_COUNT = 100;  // 排行榜最大视频数量
    private static final int POPULAR_PAGES = 10;  // 热门视频的页数
    private static final int PAGE_SIZE = 20;  // 每页爬取的视频数量
    private static final long POPULAR_CACHE_EXPIRE_TIME = 86400;  // 热门视频缓存过期时间，单位秒，1天

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private int currentPage = 1;  // 用于循环获取热门视频的页码

    // 从配置文件读取是否在项目启动时初始化数据
    @Value("${bilibili.video.init-on-startup:true}")
    private boolean initOnStartup;

    @PostConstruct
    public void init() {
        if (initOnStartup) {
            log.info("初始化 Bilibili 视频缓存池...");
            updateRankingVideos();
            updatePopularVideos();
        } else {
            log.info("项目启动时未启用数据初始化");
        }
    }

    /**
     * 获取排行榜前100的视频
     */
    @Override
    public List<BilibiliVideoDetail> getRankingVideos() {
        // 从 Redis 获取缓存的排行榜视频
        List<String> rankingVideosJson = stringRedisTemplate.opsForList().range(RANKING_VIDEOS_KEY, 0, -1);

        if (rankingVideosJson == null || rankingVideosJson.isEmpty()) {
            log.info("排行榜视频缓存为空，正在从B站获取...");
            updateRankingVideos();  // 自动更新缓存
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
        // 从 Redis 的 Hash 结构中获取所有热门视频
        List<String> popularVideosJson = stringRedisTemplate.opsForHash().values(POPULAR_VIDEOS_DETAILS_KEY).stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        // 将 JSON 字符串转换为 BilibiliVideoDetail 对象列表
        return popularVideosJson.stream()
                .map(BilibiliVideoDetail::fromJson)
                .collect(Collectors.toList());
    }

    /**
     * 定时任务：每30分钟更新一次排行榜视频缓存
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    private void scheduledUpdateRankingVideos() {
        log.info("定时更新排行榜视频缓存...");
        updateRankingVideos();
    }

    /**
     * 定时任务：每1分钟更新一次热门视频缓存
     */
    @Scheduled(cron = "0 * * * * ?")
    private void scheduledUpdatePopularVideos() {
        log.info("定时更新热门视频缓存...");
        updatePopularVideos();
    }

    /**
     * 定时任务：每天凌晨清空热门视频缓存
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void scheduledClearPopularVideos() {
        log.info("清空热门视频缓存...");
        stringRedisTemplate.delete(POPULAR_VIDEOS_SET_KEY);
        stringRedisTemplate.delete(POPULAR_VIDEOS_DETAILS_KEY);
    }

    /**
     * 更新排行榜视频缓存
     */
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
                List<String> rankingVideosJson = rankingVideos.stream().map(BilibiliVideoDetail::toJson).collect(Collectors.toList());

                stringRedisTemplate.opsForList().leftPushAll(RANKING_VIDEOS_KEY, rankingVideosJson);
                stringRedisTemplate.opsForList().trim(RANKING_VIDEOS_KEY, 0, MAX_RANKING_VIDEO_COUNT - 1);

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
     * 更新热门视频缓存（优化：批量操作 Redis）
     */
    public void updatePopularVideos() {
        try {
            // 爬取当前页的热门视频
            ApiResponse<List<VideoDetail>> response = BilibiliHotApi.getPopularVideos(currentPage, PAGE_SIZE);
            if (response.isSuccess()) {
                List<BilibiliVideoDetail> popularVideos = response.getData().stream()
                        .map(BilibiliVideoDetail::fromVideoDetail)
                        .collect(Collectors.toList());

                Map<String, String> videoDetailsMap = popularVideos.stream()
                        .collect(Collectors.toMap(BilibiliVideoDetail::getBvid, BilibiliVideoDetail::toJson));

                stringRedisTemplate.opsForSet().add(POPULAR_VIDEOS_SET_KEY, videoDetailsMap.keySet().toArray(new String[0]));
                stringRedisTemplate.opsForHash().putAll(POPULAR_VIDEOS_DETAILS_KEY, videoDetailsMap);

                stringRedisTemplate.expire(POPULAR_VIDEOS_SET_KEY, POPULAR_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
                stringRedisTemplate.expire(POPULAR_VIDEOS_DETAILS_KEY, POPULAR_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);

                log.info("成功更新热门视频缓存，当前页: " + currentPage);

                // 更新页码，循环获取 1~10 页
                currentPage = (currentPage % POPULAR_PAGES) + 1;
            } else {
                log.error("获取热门视频失败: {}", response.getMsg());
            }
        } catch (Exception e) {
            log.error("更新热门视频缓存时发生错误: {}", e.getMessage(), e);
        }
    }
}