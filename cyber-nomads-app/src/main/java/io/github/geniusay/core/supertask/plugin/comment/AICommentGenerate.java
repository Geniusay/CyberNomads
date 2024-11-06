package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import io.github.geniusay.utils.AIGenerate.BaseGenerate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.COUNT_NUM;
import static io.github.geniusay.constants.PluginConstant.PRE_TEXT;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/4 1:18
 */
@Component
public class AICommentGenerate implements CommentGenerate {
    @Resource
    AIGenerateUtil generateUtil;

    @Override
    public String generateComment(Map<String, Object> params) {
        return generateUtil.textGenerateAndReturnContent(getValue(params, PRE_TEXT, String.class),getValue(params,COUNT_NUM,Integer.class));
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams(PRE_TEXT, String.class, "文本提示词前缀"),
                new TaskNeedParams(COUNT_NUM,Integer.class,"字数限制")
        );
    }
}
