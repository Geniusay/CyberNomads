package io.github.geniusay.core.supertask.plugin.video;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.constants.RedisConstant.POPULAR_VIDEOS_DETAILS_KEY;
import static io.github.geniusay.constants.RedisConstant.POPULAR_VIDEOS_SET_KEY;
import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;

@Scope("prototype")
@Component(GET_VIDEO_GROUP_NAME)
public class GetHotVideoPlugin extends AbstractGetVideoPlugin implements GetHandleVideoPlugin {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final long VIDEO_MARK_EXPIRE_DAYS = 1;
    private static final int MAX_RETRY_COUNT = 5;
    private String scope;

    @Override
    public void init(Task task) {
        super.init(task);
        // 从 task 参数中获取作用范围，默认为 "task"
        scope = getValue(this.pluginParams, SCOPE, String.class);
    }

    @Override
    public BilibiliVideoDetail getHandleVideo(RobotWorker robot, Task task) {
        // 根据作用范围，生成 Redis 的标记 key
        String redisKey = generateRedisKey(scope, robot, task);

        // 随机获取未被标记的视频，并加入重试机制
        BilibiliVideoDetail video = getRandomUnmarkedVideo(redisKey);

        // 如果找到了未标记的视频，则打标记
        if (video != null) {
            markVideoInRedis(video, redisKey, VIDEO_MARK_EXPIRE_DAYS);
        }

        return video;
    }

    /**
     * 随机获取一个未被标记的视频，加入重试机制
     */
    private BilibiliVideoDetail getRandomUnmarkedVideo(String redisKey) {
        int retryCount = 0;

        while (retryCount < MAX_RETRY_COUNT) {
            // 从 Redis 的 Set 中随机获取一个热门视频的 bvid
            String randomBvid = stringRedisTemplate.opsForSet().randomMember(POPULAR_VIDEOS_SET_KEY);

            if (randomBvid != null) {
                Boolean isMarked = stringRedisTemplate.opsForSet().isMember(redisKey, randomBvid);

                if (Boolean.FALSE.equals(isMarked)) {
                    String videoJson = (String) stringRedisTemplate.opsForHash().get(POPULAR_VIDEOS_DETAILS_KEY, randomBvid);
                    if (videoJson != null) {
                        return BilibiliVideoDetail.fromJson(videoJson);
                    }
                }
            }

            retryCount++;
        }

        return null;
    }

    /**
     * 在 Redis 中标记视频
     */
    private void markVideoInRedis(BilibiliVideoDetail video, String redisKey, long expireDays) {
        stringRedisTemplate.opsForSet().add(redisKey, video.getBvid());
        stringRedisTemplate.expire(redisKey, expireDays, TimeUnit.DAYS);  // 设置标记的过期时间
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
                TaskNeedParams.ofKV(SCOPE, "task", "避免重复挑选视频规则，填：task/robot")
        );
    }
}