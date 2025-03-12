package io.github.geniusay.core.ai.model.siliconflow;

import io.github.geniusay.core.ai.config.SiliconflowConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.QW_72B_PREVIEW;

@Component
public class Qwen72BInstruct extends AbstractSiliconflowModel {

    public Qwen72BInstruct(SiliconflowConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return QW_72B_PREVIEW;
    }

    @Override
    public String description() {
        return "通义千问 (72B-Preview) ";
    }

    @Override
    protected String getModelVersion() {
        return "Qwen/QVQ-72B-Preview";
    }
}