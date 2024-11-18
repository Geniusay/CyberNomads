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
import static io.github.geniusay.core.supertask.task.TaskNeedParams.InputTypeEnum.TEXTAREA;

@Scope("prototype")
@Component(AI_CUSTOM_COMMENT_GENERATE)
public class AICommentGenerate extends AbstractCommentGenerate implements CommentGenerate {

    @Resource
    AIGenerateUtil generateUtil;

    // 参数
    private String combinedTemplate;  // 合并后的身份模板和提示词
    private Integer textCount;        // 生成字数上限
    private String slogan;            // Slogan 标语

    // 猫娘模板
    public static final String CATGIRL_TEMPLATE =
            "身份：可爱猫娘\n" +
            "性格：活泼、俏皮、忠诚，喜欢撒娇\n" +
            "爱好：追蝴蝶、晒太阳、陪伴主人\n" +
            "语言风格：带有猫咪的口癖，经常在句尾加上“喵~”\n" +
            "提示：请根据以上身份生成一条可爱、有趣且符合猫娘性格的评论，记得加上猫咪的口癖哦！";

    @Override
    public void init(Task task) {
        super.init(task);
        combinedTemplate = getValue(this.pluginParams, AI_PRE_TEXT, String.class);
        textCount = getValue(this.pluginParams, AI_COUNT_NUM, Integer.class);
        slogan = getValue(this.pluginParams, SLOGAN, String.class);
    }

    @Override
    public String generateComment(String content) {
        StringBuilder baseText = new StringBuilder(combinedTemplate).append("\n");

        if (content != null && !content.isEmpty()) {
            baseText.append("\n请根据上面的描述和以下的内容生成评论：").append(content);
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
                TaskNeedParams.ofKV(AI_PRE_TEXT, CATGIRL_TEMPLATE, "AI会将下面内容作为参考 (您可以自行修改)").setInputType(TEXTAREA),
                TaskNeedParams.ofKV(AI_COUNT_NUM, 100, "生成多少字"),
                TaskNeedParams.ofK(SLOGAN, String.class, "在末尾加上固定内容")
        );
    }
}