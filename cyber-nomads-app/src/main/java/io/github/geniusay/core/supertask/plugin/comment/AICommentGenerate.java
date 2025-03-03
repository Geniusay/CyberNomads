package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.ai.delegate.AIService;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static io.github.geniusay.constants.AIConstant.*;
import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.AI_CUSTOM_COMMENT_GENERATE;
import static io.github.geniusay.core.supertask.task.TaskNeedParams.InputTypeEnum.TEXTAREA;

@Scope("prototype")
@Component(AI_CUSTOM_COMMENT_GENERATE)
public class AICommentGenerate extends AbstractCommentGenerate implements CommentGenerate {

    @Resource
    AIService aiService;

    // 参数
    private String combinedTemplate;  // 合并后的身份模板和提示词
    private Integer textCount;        // 生成字数上限
    private String slogan;            // Slogan 标语
    private String aiModel;           // ai模型

    // 猫娘模板
    public static final String CATGIRL_TEMPLATE =
            "身份：可爱猫娘\n" +
                    "性格：活泼、俏皮、忠诚，喜欢撒娇\n" +
                    "爱好：追蝴蝶、晒太阳、陪伴主人\n" +
                    "语言风格：带有猫咪的口癖，经常在句尾加上“喵~”\n" +
                    "提示：请根据以上身份生成一条可爱、有趣且符合猫娘性格的评论，记得加上猫咪的口癖哦！";

    public static final String SLOGAN_TEMPLATE = "我是来自CyberNomads的小伙伴，快来加入我们的大家庭：534807469";

    @Override
    public void init(Task task) {
        super.init(task);
        combinedTemplate = getValue(this.pluginParams, AI_PRE_TEXT, String.class);
        textCount = getValue(this.pluginParams, AI_COUNT_NUM, Integer.class);
        slogan = getValue(this.pluginParams, SLOGAN, String.class);
        aiModel = getValue(this.pluginParams, AI_MODEL, String.class);
    }

    @Override
    public String generateComment(String content) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("combinedTemplate", combinedTemplate);
        params.put("textCount", textCount);
        params.put("slogan", slogan);

        Object comment = aiService.execute(ROLE_COMMENT, aiModel, params);

        // 生成最终评论
        return comment.toString();
    }

    @Override
    public String generateComment() {
        return generateComment("");
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(AI_PRE_TEXT, CATGIRL_TEMPLATE, "AI会将下面内容作为参考 (您可以自行修改)").setInputType(TEXTAREA),
                TaskNeedParams.ofKV(AI_COUNT_NUM, 100, "生成多少字").setExtendDesc(AI_COUNT_NUM_EXT_DESC),
                TaskNeedParams.ofKV(SLOGAN, SLOGAN_TEMPLATE, "Slogan结尾语").setExtendDesc(SLOGAN_EXT_DESC),
                TaskNeedParams.ofSelection(AI_MODEL, QW_PLUS, "请选择你的AI模型",
                        aiService.getTaskNeedParams(
                                List.of(QW_PLUS, DEEP_SEEK_R1, DEEP_SEEK_V3, QW_72B_PREVIEW, QW_72B_INSTRUCT))
                        , AI_MODEL_DESC, false)
        );
    }
}
