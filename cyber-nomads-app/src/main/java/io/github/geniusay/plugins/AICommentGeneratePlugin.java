package io.github.geniusay.plugins;

import io.github.geniusay.core.supertask.plugin.comment.CommentGenerate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AICommentGeneratePlugin implements CommentGenerate {

    @Override
    public String generateComment(Map<String, Object> params) {
        return "";
    }
}
