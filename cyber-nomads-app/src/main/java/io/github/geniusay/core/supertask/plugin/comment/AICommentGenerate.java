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
public class AICommentGenerate extends AbstractCommentGenerate implements CommentGenerate {

    @Resource
    AIGenerateUtil generateUtil;

    // 参数
    private String identityTemplate;  // 身份模板
    private String preText;           // 提示词
    private Integer textCount;        // 生成字数上限
    private String slogan;            // Slogan 标语
    private Boolean basedOnContent;   // 是否根据内容生成

    // 默认身份模板
    public static final String DEFAULT_IDENTITY_TEMPLATE =
            "身份：B站资深用户\n" +
            "年龄：25岁\n" +
            "性格：风趣幽默，富有洞察力\n" +
            "资历：5年B站活跃经验\n" +
            "回复风格：简明扼要，观点鲜明\n";

    @Override
    public void init(Task task) {
        super.init(task);
        identityTemplate = getValue(this.pluginParams, IDENTITY_TEMPLATE, String.class);
        preText = getValue(this.pluginParams, AI_PRE_TEXT, String.class);
        textCount = getValue(this.pluginParams, AI_COUNT_NUM, Integer.class);
        slogan = getValue(this.pluginParams, SLOGAN, String.class);
        basedOnContent = getValue(this.pluginParams, BASED_ON_CONTENT, Boolean.class);
    }

    @Override
    public String generateComment(String content) {
        StringBuilder baseText = new StringBuilder(identityTemplate).append("\n");

        baseText.append("\n额外信息：").append(preText);

        if (basedOnContent && content != null && !content.isEmpty()) {
            baseText.append("\n请用以上描述的身份加上以下的概述输出内容：").append(content);
        }

        return generateUtil.textGenerateAndReturnContent(baseText.toString(), textCount, slogan);
    }

    @Override
    public String generateComment() {
        return generateComment("");
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(IDENTITY_TEMPLATE, DEFAULT_IDENTITY_TEMPLATE, "身份模板：您将以此形象进行评论").setInputType(TaskNeedParams.InputTypeEnum.TEXTAREA),
                TaskNeedParams.ofKV(AI_PRE_TEXT, "请输入提示词", "提示词：作为AI参考的额外信息").setInputType(TaskNeedParams.InputTypeEnum.TEXTAREA),
                TaskNeedParams.ofKV(BASED_ON_CONTENT, false, "是否参考视频内容，默认关闭"),
                TaskNeedParams.ofKV(AI_COUNT_NUM, 100, "评论字数上限"),
                TaskNeedParams.ofK(SLOGAN, String.class, "在结尾处追加此Slogan标语")
        );
    }
}