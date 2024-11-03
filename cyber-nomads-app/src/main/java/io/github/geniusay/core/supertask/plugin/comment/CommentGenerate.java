package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;

import java.util.Map;

public interface CommentGenerate extends TaskPlugin {
    String generateComment(Map<String, Object> params);
}
