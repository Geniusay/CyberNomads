package io.github.geniusay.core.ai.model.siliconflow;

import io.github.geniusay.core.ai.config.SiliconflowConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.DEEP_SEEK_V3;

@Component
public class DeepSeekV3 extends AbstractSiliconflowModel {

    public DeepSeekV3(SiliconflowConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return DEEP_SEEK_V3;
    }

    @Override
    public String description() {
        return "深度求索 (DeepSeek V3) ";
    }

    @Override
    protected String getModelVersion() {
        return "Pro/deepseek-ai/DeepSeek-V3";
    }
}