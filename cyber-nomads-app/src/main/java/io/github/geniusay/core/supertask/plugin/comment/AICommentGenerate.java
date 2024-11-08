package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.AI_COMMENT_GENERATE_PLUGIN;


/**
 * @Description
 * @Author welsir
 * @Date 2024/11/4 1:18
 */
@Component(AI_COMMENT_GENERATE_PLUGIN)
public class AICommentGenerate extends AbstractCommentGenerate implements CommentGenerate {
    @Resource
    AIGenerateUtil generateUtil;


    @Override
    public String generateComment() {
        Map<String, Object> params = this.pluginParams;
        String aiPreText = getValue(params, AI_PRE_TEXT, String.class);
        if (getValue(params, AI_START, Boolean.class)) {
            Integer aiCountNum = getValue(params, AI_COUNT_NUM, Integer.class);
            return generateUtil.textGenerateAndReturnContent(aiPreText, aiCountNum, getValue(params, SLOGAN, String.class));
        }
        return aiPreText;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams(AI_START, Boolean.class, "是否开启AI生成"),
                new TaskNeedParams(AI_PRE_TEXT, String.class, "文本提示词前缀"),
                new TaskNeedParams(AI_COUNT_NUM, Integer.class, "字数限制"),
                new TaskNeedParams(SLOGAN, String.class, "slogan标语，结尾处另起一行追加", false, "")
        );
    }

}
