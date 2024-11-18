package io.github.geniusay.core.supertask.plugin.video;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.crawler.api.bilibili.BilibiliHotApi;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.constants.RedisConstant.POPULAR_VIDEOS_DETAILS_KEY;
import static io.github.geniusay.constants.RedisConstant.POPULAR_VIDEOS_SET_KEY;
import static io.github.geniusay.core.supertask.config.PluginConstant.HOT_VIDEO_PLUGIN;

@Slf4j
@Scope("prototype")
@Component(HOT_VIDEO_PLUGIN)
public class GetHotVideoPlugin extends AbstractGetVideoPlugin implements GetHandleVideoPlugin {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final long VIDEO_MARK_EXPIRE_DAYS = 1;
    private static final int MAX_RETRY_COUNT = 2;  // 最大重试次数
    private String scope;

    private static final Map<String, String> SCOPE_MAPPING = Map.of(
            "空间", "task",
            "视频", "robot"
    );

    @Override
    public void init(Task task) {
        super.init(task);
        String selectedSource = getValue(this.pluginParams, SCOPE, String.class);
        scope = SCOPE_MAPPING.getOrDefault(selectedSource, "task");
    }

    @Override
    public BilibiliVideoDetail getHandleVideo(RobotWorker robot, Task task) {
        // 根据作用范围，生成 Redis 的标记 key
        String redisKey = generateRedisKey(scope, robot, task);

        try {
            // 随机获取未被标记的视频，并加入重试机制
            BilibiliVideoDetail video = getRandomUnmarkedVideo(redisKey);

            if (video != null) {
                markVideoInRedis(video, redisKey, VIDEO_MARK_EXPIRE_DAYS);
            } else {
                ApiResponse<List<VideoDetail>> popularVideos = BilibiliHotApi.getPopularVideos(1, 3);
                if (popularVideos.isSuccess()) {
                    video = BilibiliVideoDetail.fromVideoDetail(popularVideos.getData().get(0));
                } else {
                    return getHandleVideo(robot, task);
                }
            }
            return video;
        } catch (Exception e) {
            log.error("获取视频时发生异常，任务: {}，Robot: {}，异常信息: {}", task.getId(), robot.getId(), e.getMessage(), e);
        }
        return null;
    }

    /**
     * 使用 Lua 脚本随机获取一个视频，加入重试机制。
     * 如果达到最大重试次数后，返回最后一次获取的视频（无论是否已标记）。
     * 若未获取到任何视频，则清除 Redis 中标记的所有视频。
     */
    private BilibiliVideoDetail getRandomUnmarkedVideo(String redisKey) {
        String luaScript =
                "local retries = tonumber(ARGV[1]) " +
                        "for i = 1, retries do " +
                        "   local bvid = redis.call('SRANDMEMBER', KEYS[1]) " +
                        "   if not bvid then return nil end " +
                        "   local isMarked = redis.call('SISMEMBER', KEYS[2], bvid) " +
                        "   if isMarked == 0 then " +
                        "       local videoJson = redis.call('HGET', KEYS[3], bvid) " +
                        "       if videoJson then return videoJson end " +
                        "   end " +
                        "end " +
                        "return nil";

        try {
            // 执行 Lua 脚本获取随机视频
            String videoJson = stringRedisTemplate.execute(
                    (RedisScript<String>) RedisScript.of(luaScript, String.class),
                    List.of(POPULAR_VIDEOS_SET_KEY, redisKey, POPULAR_VIDEOS_DETAILS_KEY),
                    String.valueOf(MAX_RETRY_COUNT)
            );

            if (videoJson != null) {
                return BilibiliVideoDetail.fromJson(videoJson);
            }
            log.warn("达到最大重试次数，未获取到任何视频，清除 Redis 标记: {}", redisKey);
            clearMarkedVideos(redisKey);
        } catch (Exception e) {
            log.error("执行 Lua 脚本获取视频时发生异常，异常信息: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 使用 Lua 脚本在 Redis 中标记视频
     */
    private void markVideoInRedis(BilibiliVideoDetail video, String redisKey, long expireDays) {
        String luaScript =
                "redis.call('SADD', KEYS[1], ARGV[1]) " +
                        "redis.call('EXPIRE', KEYS[1], ARGV[2])";

        try {
            // 执行 Lua 脚本标记视频
            stringRedisTemplate.execute(
                    (RedisScript<Void>) RedisScript.of(luaScript, Void.class),
                    List.of(redisKey),
                    video.getBvid(), String.valueOf(TimeUnit.DAYS.toSeconds(expireDays))
            );
        } catch (Exception e) {
            log.error("执行 Lua 脚本标记视频时发生异常，bvid: {}，异常信息: {}", video.getBvid(), e.getMessage(), e);
        }
    }

    /**
     * 清除 Redis 中标记的所有视频
     */
    private void clearMarkedVideos(String redisKey) {
        try {
            stringRedisTemplate.delete(redisKey);
            log.info("成功清除 Redis 中的标记: {}", redisKey);
        } catch (Exception e) {
            log.error("清除 Redis 中标记时发生异常，Redis Key: {}，异常信息: {}", redisKey, e.getMessage(), e);
        }
    }

    /**
     * 根据作用范围生成 Redis 的标记 key
     */
    private String generateRedisKey(String scope, RobotWorker robot, Task task) {
        if ("robot".equals(scope)) {
            return String.format("bili:video:robot:%s:handled", robot.getId());
        } else {
            return String.format("bili:video:task:%s:handled", task.getId());
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofSelection(SCOPE, "任务级别", "避免重复规则", List.of(
                        TaskNeedParams.ofK("任务级别", String.class, "任务级别"),
                        TaskNeedParams.ofK("机器人级别", String.class, "机器人级别")
                ))
        );
    }
}