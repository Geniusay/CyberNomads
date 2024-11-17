package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.AI_REPLY_COMMENT_GENERATE;

@Scope("prototype")
@Component(AI_REPLY_COMMENT_GENERATE)
public class AIReplyCommentGenerate extends AbstractCommentGenerate implements CommentGenerate {

    @Resource
    AIGenerateUtil generateUtil;

    private String preText;
    private Integer textCount;
    private String slogan;

    // 优雅整洁的模板字符串，帮助用户理解如何填写身份信息
    public static final String DEFAULT_AI_PRE_TEXT_TEMPLATE =
            "身份：B站资深用户\n" +
            "年龄：25岁\n" +
            "性格：风趣幽默，富有洞察力\n" +
            "资历：5年B站活跃经验\n" +
            "回复风格：简明扼要，观点鲜明\n";

    @Override
    public void init(Task task) {
        super.init(task);
        preText = getValue(this.pluginParams, AI_PRE_TEXT, String.class);
        textCount = getValue(this.pluginParams, AI_COUNT_NUM, Integer.class);
        slogan = getValue(this.pluginParams, SLOGAN, String.class);
    }

    @Override
    public String generateComment(String content) {
        String temp = preText + " 请用以上描述的身份回复以下评论：" + content;
        return generateUtil.textGenerateAndReturnContent(temp, textCount, slogan);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(AI_PRE_TEXT, DEFAULT_AI_PRE_TEXT_TEMPLATE, "请指定以什么身份回复，可以参考以下模版：").setInputType(TaskNeedParams.InputTypeEnum.TEXTAREA),
                TaskNeedParams.ofKV(AI_COUNT_NUM, 50, "生成字数上限"),
                TaskNeedParams.ofK(SLOGAN, String.class, "Slogan标语在结尾处追加")
        );
    }
}