package io.github.geniusay.core.supertask.config;

import java.util.Map;

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.*;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.*;

public class TaskTranslationConstant {

    // 平台中英文对照表
    public static final Map<String, String> PLATFORM_TRANSLATION = Map.of(
            BILIBILI, "哔哩哔哩",
            DOUYING, "抖音"
    );

    // 任务类型中英文对照表
    public static final Map<String, String> TASK_TYPE_TRANSLATION = Map.of(
            VIDEO_COMMENT, "对某个视频评论",
            VIDEO_LIKE, "对某个视频点赞",
            VIDEO_COIN, "对某个视频投币",
            HOT_VIDEO_COMMENT, "在热门视频下面进行评论"
    );
}