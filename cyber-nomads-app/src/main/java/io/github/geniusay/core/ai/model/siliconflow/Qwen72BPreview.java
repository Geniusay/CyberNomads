package io.github.geniusay.core.ai.model.siliconflow;

import io.github.geniusay.core.ai.config.SiliconflowConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.QW_72B_INSTRUCT;

@Component
public class Qwen72BPreview extends AbstractSiliconflowModel {

    public Qwen72BPreview(SiliconflowConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return QW_72B_INSTRUCT;
    }

    @Override
    public String description() {
        return "通义千问 (72B-Instruct) ";
    }

    @Override
    protected String getModelVersion() {
        return "Qwen/Qwen2-VL-72B-Instruct";
    }
}