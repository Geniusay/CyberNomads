package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.AI_CUSTOM_COMMENT_GENERATE;

@Scope("prototype")
@Component(AI_CUSTOM_COMMENT_GENERATE)
public class AICustomCommentGenerate extends AbstractCommentGenerate implements CommentGenerate {

    @Resource
    AIGenerateUtil generateUtil;
    private String preText;
    private Integer textCount;
    private String slogan;

    @Override
    public void init(Task task) {
        super.init(task);
        preText = getValue(this.pluginParams, AI_PRE_TEXT, String.class);
        textCount = getValue(this.pluginParams, AI_COUNT_NUM, Integer.class);
        slogan = getValue(this.pluginParams, SLOGAN, String.class);
    }

    @Override
    public String generateComment() {
        return generateUtil.textGenerateAndReturnContent(preText, textCount, slogan);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
            TaskNeedParams.ofK(AI_PRE_TEXT, String.class, "文本提示词前缀").setInputType(TaskNeedParams.InputTypeEnum.TEXTAREA),
            TaskNeedParams.ofKV(AI_COUNT_NUM, 50, "生成字数上限"),
            TaskNeedParams.ofK(SLOGAN, String.class, "Slogan标语在结尾处追加")
        );
    }
}
