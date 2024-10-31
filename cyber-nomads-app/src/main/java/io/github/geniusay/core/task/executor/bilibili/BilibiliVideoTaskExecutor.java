package io.github.geniusay.core.task.executor.bilibili;

import io.github.geniusay.core.task.executor.SimpleTaskExecutor;
import io.github.geniusay.core.task.enums.TaskType;
import io.github.geniusay.core.task.po.SimpleTask;

/**
 * 描述: b站视频相关任务执行器
 * @author suifeng
 * 日期: 2024/10/30
 */
public class BilibiliVideoTaskExecutor extends SimpleTaskExecutor {

    @Override
    public int executeSimpleTask(SimpleTask simpleTask) {
        // 获取对应的任务类型
        TaskType type = simpleTask.getType();

        // 根据任务类型执行不同的逻辑
        switch (type) {
            case VIDEO_LIKE:
                return likeVideo(simpleTask);
            case VIDEO_FAV:
                return favVideo(simpleTask);
            case VIDEO_COIN:
                return coinVideo(simpleTask);
            case VIDEO_COMMENT:
                return commentVideo(simpleTask);
            default:
                System.out.println("Unknown task type: " + type);
                return 0; // 未知任务，返回失败
        }
    }

    // 点赞视频的逻辑
    private int likeVideo(SimpleTask task) {
        System.out.println("Liking video with ID: " + task.getId());
        // 模拟点赞操作成功
        return 1; // 返回1表示成功
    }

    // 收藏视频的逻辑
    private int favVideo(SimpleTask task) {
        System.out.println("Favorite video with ID: " + task.getId());
        // 模拟收藏操作成功
        return 1; // 返回1表示成功
    }

    // 投币视频的逻辑
    private int coinVideo(SimpleTask task) {
        System.out.println("Coining video with ID: " + task.getId());
        // 模拟投币操作成功
        return 1; // 返回1表示成功
    }


    // 评论视频的逻辑
    private int commentVideo(SimpleTask task) {
        System.out.println("Commenting on video with ID: " + task.getId());
        // 模拟评论操作成功
        return 1; // 返回1表示成功
    }
}
