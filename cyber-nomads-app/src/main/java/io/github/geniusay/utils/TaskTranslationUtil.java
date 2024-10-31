package io.github.geniusay.utils;

import io.github.geniusay.core.supertask.config.TaskTranslationConstant;

public class TaskTranslationUtil {

    private TaskTranslationUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 将平台名称从英文转换为中文
     * @param platform 英文平台名称
     * @return 中文平台名称
     */
    public static String translatePlatform(String platform) {
        return TaskTranslationConstant.PLATFORM_TRANSLATION.getOrDefault(platform, platform);
    }

    /**
     * 将任务类型从英文转换为中文
     * @param taskType 英文任务类型
     * @return 中文任务类型
     */
    public static String translateTaskType(String taskType) {
        return TaskTranslationConstant.TASK_TYPE_TRANSLATION.getOrDefault(taskType, taskType);
    }
}