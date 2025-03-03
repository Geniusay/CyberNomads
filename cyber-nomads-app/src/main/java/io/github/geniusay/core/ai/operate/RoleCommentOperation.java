package io.github.geniusay.core.ai.operate;

import io.github.geniusay.core.ai.core.AIOp;
import io.github.geniusay.core.ai.core.AIOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static io.github.geniusay.constants.AIConstant.*;

@AIOp(ROLE_COMMENT)
@Component
public class RoleCommentOperation implements AIOperation<HashMap<String, Object>, String> {

    private HashMap<String, Object> params;

    private final static int DEFAULT_MAX_NUM = 200;

    @Override
    public List<String> supportedModels() {
        return List.of(QW_PLUS, DEEP_SEEK_V3, DEEP_SEEK_R1);
    }

    @Override
    public String buildPrompt(HashMap<String, Object> params) {
        this.params = params;

        String content = params.get("content").toString();
        String combinedTemplate = params.get("combinedTemplate").toString();
        int textCount = (int) params.get("textCount");

        StringBuilder baseText = new StringBuilder();

        if (content != null && !content.isEmpty()) {
            baseText.append("你刚刚看了观看了一个视频，但是你看不到真正的视频，所以我只能给你下面这个视频的内容总结\n");
            baseText.append(content).append("\n\n");
        }

        baseText.append("你作为一个：").append(combinedTemplate).append("\n");

        baseText.append("请你");
        if (content != null && !content.isEmpty()) {
            baseText.append("对这个视频");
        }
        baseText.append("进行高质量的评论");

        textCount = Math.max(textCount, DEFAULT_MAX_NUM);
        baseText.append("请将字数控制在").append(textCount).append("字以内");

        return String.valueOf(baseText);
    }

    @Override
    public String parseResponse(String aiResponse) {
        String slogan = params.get("slogan").toString();
        return aiResponse + "\n" + slogan;
    }
}
