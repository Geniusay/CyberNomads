package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;

/**
 * 评论内容生成插件接口
 */
public interface CommentGenerate extends TaskPlugin {

    default String generateComment() {
        return "";
    }

    default String generateComment(RobotWorker robot) {
        return generateComment();
    }

    default String generateComment(String content) {
        return generateComment();
    }
}
