package io.github.geniusay.core.ai.model.siliconflow;

import io.github.geniusay.core.ai.config.SiliconflowConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.DEEP_SEEK_R1;

@Component
public class DeepSeekR1 extends AbstractSiliconflowModel {

    public DeepSeekR1(SiliconflowConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return DEEP_SEEK_R1;
    }

    @Override
    public String description() {
        return "深度求索 (DeepSeek R1) 深度思考";
    }

    @Override
    protected String getModelVersion() {
        return "Pro/deepseek-ai/DeepSeek-R1";
    }
}