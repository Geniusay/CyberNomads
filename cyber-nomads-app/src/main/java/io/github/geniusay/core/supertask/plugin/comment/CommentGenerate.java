package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;

import java.util.Map;

import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;

/**
 * 评论内容生成插件接口
 */
public interface CommentGenerate extends TaskPlugin {
    String generateComment();

}
