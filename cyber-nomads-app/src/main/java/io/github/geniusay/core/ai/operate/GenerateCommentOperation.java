package io.github.geniusay.core.ai.operate;

import io.github.geniusay.core.ai.core.AIOp;
import io.github.geniusay.core.ai.core.AIOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static io.github.geniusay.constants.AIConstant.GENERATE_COMMENT;
import static io.github.geniusay.constants.AIConstant.QW_MODEL;

@AIOp(GENERATE_COMMENT)
@Component
public class GenerateCommentOperation implements AIOperation<HashMap<String, Object>, String> {

    private HashMap<String, Object> params;

    @Override
    public List<String> supportedModels() {
        return List.of(QW_MODEL);
    }

    @Override
    public String buildPrompt(HashMap<String, Object> params) {
        this.params = params;

        String content = params.get("content").toString();
        String combinedTemplate = params.get("combinedTemplate").toString();
        String textCount = params.get("textCount").toString();

        StringBuilder baseText = new StringBuilder();

        if (content != null && !content.isEmpty()) {
            baseText.append("你刚刚看了下面这个视频的内容总结\n");
            baseText.append(content).append("\n\n");
        }

        baseText.append("你作为一个").append(combinedTemplate).append("\n");
        baseText.append("请你对这个视频进行评论");
        baseText.append("请将字数控制在").append(textCount).append("字以内");

        return String.valueOf(baseText);
    }

    @Override
    public String parseResponse(String aiResponse) {
        String slogan = params.get("slogan").toString();
        return aiResponse + "\n" + slogan;
    }
}
