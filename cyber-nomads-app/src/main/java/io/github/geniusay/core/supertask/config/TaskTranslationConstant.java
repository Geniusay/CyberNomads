package io.github.geniusay.core.supertask.config;

import java.util.Map;

import static io.github.geniusay.constants.TerminatorConstants.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.*;
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
            INFINITY_HOT_VIDEO_COMMENT, "永无止境的在热门视频下面进行评论",
            TEST_VIDEO_COMMENT, "永久任务测试"
    );

    // 插件中文名
    public static final Map<String, String> PLUGIN_TRANSLATION = Map.of(
            TERMINATOR_GROUP_NAME,"终止器(任务执行次数规则)",
            COMMENT_GROUP_NAME,"评论内容生成器",
            GET_VIDEO_GROUP_NAME,"视频选择器",
            AI_COMMENT_GENERATE_PLUGIN, "AI生成插件",
            TERMINATOR_TYPE_GROUP_COUNT, "总计数器",
            TERMINATOR_TYPE_TIMES, "计数器(每个赛博人单独计数)",
            COOL_DOWN_TYPE_TIMES, "定时器"
    );

    public static String getTranslation(Map<String, String> translationMap, String name){
        return translationMap.getOrDefault(name, name);
    }
}
