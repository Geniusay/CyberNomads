package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;

import java.util.List;
import java.util.Map;

public interface GetHandleVideo<T> extends TaskPlugin {
    List<T> getHandleVideo(Map<String, Object> params);
}
