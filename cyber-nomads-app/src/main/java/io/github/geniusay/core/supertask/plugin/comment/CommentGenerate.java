package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.pojo.DO.RobotDO;

import java.util.Map;

import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;

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
