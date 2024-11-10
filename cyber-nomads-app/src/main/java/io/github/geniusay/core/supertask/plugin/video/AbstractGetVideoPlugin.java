package io.github.geniusay.core.supertask.plugin.video;

import io.github.geniusay.core.supertask.plugin.BaseTaskPlugin;

import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;

public abstract class AbstractGetVideoPlugin extends BaseTaskPlugin implements GetHandleVideoPlugin {

    @Override
    public String getPluginGroup(){
        return GET_VIDEO_GROUP_NAME;
    }
}
