package io.github.geniusay.core.supertask.plugin.video;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;

import java.util.List;
import java.util.Map;

import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;

public interface GetHandleVideoPlugin<T> extends TaskPlugin {

    List<T> getHandleVideo(Map<String, Object> params);


}
