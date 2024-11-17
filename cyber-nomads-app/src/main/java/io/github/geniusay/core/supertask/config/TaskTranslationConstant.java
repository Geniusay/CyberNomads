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
            INFINITY_HOT_VIDEO_COMMENT, "评论热门视频：机器人持续在热门视频下发表评论",
            INFINITY_HOT_COMMENT_REPLY, "回复热门评论：机器人持续回复热门评论",
            TEST_VIDEO_COMMENT, "测试视频评论：用于测试的评论任务",
            VIDEO_INTERACTION, "视频互动任务：机器人执行点赞、投币或三连操作",
            USER_INTERACTION, "用户互动任务：机器人关注指定用户",
            SINGLE_VIDEO_CUSTOM_COMMENT, "单视频定制评论：机器人在指定视频下发布自定义评论"
    );

    // 插件名称中英文对照表
    public static final Map<String, String> PLUGIN_TRANSLATION = initPluginTranslation();

    private static Map<String, String> initPluginTranslation() {
        Map<String, String> pluginTranslation = new LinkedHashMap<>();

        // 终止器相关插件
        pluginTranslation.put(TERMINATOR_GROUP_NAME, "任务终止规则：控制任务何时结束");
        pluginTranslation.put(TERMINATOR_TYPE_GROUP_COUNT, "总次数终止器：任务达到指定次数后结束");
        pluginTranslation.put(TERMINATOR_TYPE_TIMES, "单次执行终止器：任务执行一次后结束");
        pluginTranslation.put(COOL_DOWN_TYPE_TIMES, "冷却时间终止器：任务执行后冷却一段时间");
        pluginTranslation.put(SINGLE_USE, "一次性终止器：每个机器人只能执行一次");

        // 评论生成器
        pluginTranslation.put(COMMENT_GROUP_NAME, "评论生成器：生成评论内容的插件");
        pluginTranslation.put(AI_CUSTOM_COMMENT_GENERATE, "AI自定义评论：根据提示词生成评论");
        pluginTranslation.put(CUSTOM_COMMENT_GENERATE, "定制评论：发布固定的自定义评论");

        // 视频选择器
        pluginTranslation.put(GET_VIDEO_GROUP_NAME, "视频选择插件：选择任务操作的视频");
        pluginTranslation.put(HOT_VIDEO_PLUGIN, "随机热门视频：选择随机热门视频作为任务目标");

        // 评论选择器
        pluginTranslation.put(GET_COMMENT_GROUP_NAME, "评论选择插件：选择任务操作的评论");
        pluginTranslation.put(HOT_COMMENT_PLUGIN, "随机热门评论：选择随机热门评论作为任务目标");

        // 行为逻辑选择器
        pluginTranslation.put(LOGIC_SELECTOR_GROUP_NAME, "操作选择器：定义任务执行的操作");
        pluginTranslation.put(LIKE_LOGIC_SELECTOR, "点赞操作：机器人对视频或评论点赞");
        pluginTranslation.put(COIN_LOGIC_SELECTOR, "投币操作：机器人向视频投币（最多两枚）");
        pluginTranslation.put(TRIPLET_LOGIC_SELECTOR, "三连操作：机器人点赞、投币和收藏");
        pluginTranslation.put(FOLLOW_LOGIC_SELECTOR, "关注操作：机器人关注指定用户");
        pluginTranslation.put(COMMENT_LOGIC_SELECTOR, "评论操作：机器人发布评论");

        // 接收者选择器
        pluginTranslation.put(RECEIVER_SELECTOR_GROUP_NAME, "目标选择器：选择任务操作的目标");
        pluginTranslation.put(VIDEO_RECEIVER_SELECTOR, "指定视频：任务目标为指定视频");
        pluginTranslation.put(USER_RECEIVER_SELECTOR, "指定用户：任务目标为指定用户");
        pluginTranslation.put(COMMENT_RECEIVER_SELECTOR, "评论目标：任务目标为指定评论");

        return pluginTranslation;
    }

    // 获取翻译
    public static String getTranslation(Map<String, String> translationMap, String name) {
        return translationMap.getOrDefault(name, name);
    }
}