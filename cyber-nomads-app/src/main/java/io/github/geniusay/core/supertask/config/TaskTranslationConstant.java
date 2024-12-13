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
            INFINITY_HOT_VIDEO_SUMMARY, "AI总结视频",
            INFINITY_HOT_VIDEO_COMMENT, "AI自动评论",
            INFINITY_HOT_COMMENT_REPLY, "自动怼人",
            VIDEO_COMMENT, "视频评论",
            TEST_VIDEO_COMMENT, "测试",
            VIDEO_INTERACTION, "点赞，投币，三连",
            USER_INTERACTION, "涨粉丝",
            SINGLE_VIDEO_CUSTOM_COMMENT, "打波广告"
    );

    // 插件名称中英文对照表
    public static final Map<String, String> PLUGIN_TRANSLATION = initPluginTranslation();

    private static Map<String, String> initPluginTranslation() {
        Map<String, String> pluginTranslation = new LinkedHashMap<>();

        // 终止器相关插件
        pluginTranslation.put(TERMINATOR_GROUP_NAME, "结束规则");
        pluginTranslation.put(SINGLE_USE, "每个机器人做一次后结束");
        pluginTranslation.put(COOL_DOWN_TYPE_TIMES, "不会结束，会一直做下去");

        // 评论生成器
        pluginTranslation.put(COMMENT_GROUP_NAME, "评论生成器");
        pluginTranslation.put(AI_CUSTOM_COMMENT_GENERATE, "AI自定义评论");
        pluginTranslation.put(CUSTOM_COMMENT_GENERATE, "定制评论");

        // 视频选择器
        pluginTranslation.put(GET_VIDEO_GROUP_NAME, "视频选择器");
        pluginTranslation.put(HOT_VIDEO_PLUGIN, "随机热门视频");
        pluginTranslation.put(KEYWORD_SEARCH_PLUGIN, "关键词相关视频");

        // 评论选择器
        pluginTranslation.put(GET_COMMENT_GROUP_NAME, "评论对象");
        pluginTranslation.put(HOT_COMMENT_PLUGIN, "随机热门评论");

        // 行为逻辑选择器
        pluginTranslation.put(LOGIC_SELECTOR_GROUP_NAME, "操作");
        pluginTranslation.put(LIKE_LOGIC_SELECTOR, "点赞");
        pluginTranslation.put(COIN_LOGIC_SELECTOR, "投币");
        pluginTranslation.put(TRIPLET_LOGIC_SELECTOR, "三连");
        pluginTranslation.put(FOLLOW_LOGIC_SELECTOR, "关注");
        pluginTranslation.put(COMMENT_LOGIC_SELECTOR, "评论");

        // 接收者选择器
        pluginTranslation.put(RECEIVER_SELECTOR_GROUP_NAME, "目标");
        pluginTranslation.put(VIDEO_RECEIVER_SELECTOR, "视频");
        pluginTranslation.put(USER_RECEIVER_SELECTOR, "用户");
        pluginTranslation.put(COMMENT_RECEIVER_SELECTOR, "评论");

        return pluginTranslation;
    }

    // 获取翻译
    public static String getTranslation(Map<String, String> translationMap, String name) {
        return translationMap.getOrDefault(name, name);
    }
}
