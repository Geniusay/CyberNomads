package io.github.geniusay.core.supertask.plugin.reply;

import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;

import static io.github.geniusay.core.supertask.config.PluginConstant.GET_COMMENT_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;

public abstract class AbstractGetCommentPlugin extends BaseTaskPlugin implements GetHandleCommentPlugin {

    @Override
    public String getPluginGroup(){
        return GET_COMMENT_GROUP_NAME;
    }
}
