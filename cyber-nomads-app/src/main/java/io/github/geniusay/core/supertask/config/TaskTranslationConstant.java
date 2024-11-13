package io.github.geniusay.core.supertask.config;

import java.util.LinkedHashMap;
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
            VIDEO_COMMENT, "评论视频",
            VIDEO_LIKE, "点赞视频",
            VIDEO_COIN, "投币视频",
            INFINITY_HOT_VIDEO_COMMENT, "无限评论热门视频",
            TEST_VIDEO_COMMENT, "测试视频评论",
            VIDEO_INTERACTION, "互动任务（点赞、投币、三连）",
            USER_INTERACTION, "互动任务（关注）"
    );

    // 插件名称中英文对照表
    public static final Map<String, String> PLUGIN_TRANSLATION = initPluginTranslation();

    private static Map<String, String> initPluginTranslation() {
        Map<String, String> pluginTranslation = new LinkedHashMap<>();

        // 终止器相关插件
        pluginTranslation.put(TERMINATOR_GROUP_NAME, "任务终止规则");
        pluginTranslation.put(TERMINATOR_TYPE_GROUP_COUNT, "总次数终止器");
        pluginTranslation.put(TERMINATOR_TYPE_TIMES, "单次执行终止器");
        pluginTranslation.put(COOL_DOWN_TYPE_TIMES, "冷却时间终止器");
        pluginTranslation.put(SINGLE_USE, "一次性任务终止器");

        // 评论生成器
        pluginTranslation.put(COMMENT_GROUP_NAME, "评论生成器");
        pluginTranslation.put(AI_COMMENT_GENERATE_PLUGIN, "AI自动评论");

        // 视频选择器
        pluginTranslation.put(GET_VIDEO_GROUP_NAME, "视频选择插件");
        pluginTranslation.put(HOT_VIDEO_PLUGIN, "随机热门视频");

        // 行为逻辑选择器
        pluginTranslation.put(LOGIC_SELECTOR_GROUP_NAME, "操作选择器");
        pluginTranslation.put(LIKE_LOGIC_SELECTOR, "点赞操作");
        pluginTranslation.put(COIN_LOGIC_SELECTOR, "投币操作");
        pluginTranslation.put(TRIPLET_LOGIC_SELECTOR, "三连操作");
        pluginTranslation.put(FOLLOW_LOGIC_SELECTOR, "关注操作");

        // 接收者选择器
        pluginTranslation.put(RECEIVER_SELECTOR_GROUP_NAME, "目标选择器");
        pluginTranslation.put(VIDEO_RECEIVER_SELECTOR, "选择视频");
        pluginTranslation.put(USER_RECEIVER_SELECTOR, "选择用户");

        return pluginTranslation;
    }

    // 获取翻译
    public static String getTranslation(Map<String, String> translationMap, String name) {
        return translationMap.getOrDefault(name, name);
    }
}