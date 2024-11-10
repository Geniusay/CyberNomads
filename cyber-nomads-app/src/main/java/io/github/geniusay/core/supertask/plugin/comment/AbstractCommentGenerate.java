package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;

import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;

public abstract class AbstractCommentGenerate extends BaseTaskPlugin implements CommentGenerate {

    @Override
    public String getPluginGroup(){
        return COMMENT_GROUP_NAME;
    }
}
