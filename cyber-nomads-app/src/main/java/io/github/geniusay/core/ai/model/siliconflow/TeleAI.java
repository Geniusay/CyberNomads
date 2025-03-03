package io.github.geniusay.core.ai.model.siliconflow;

import io.github.geniusay.core.ai.config.SiliconflowConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.TELE_AI;

@Component
public class TeleAI extends AbstractSiliconflowModel {

    public TeleAI(SiliconflowConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return TELE_AI;
    }

    @Override
    public String description() {
        return "中国电信 (TeleChat2) ";
    }

    @Override
    protected String getModelVersion() {
        return "TeleAI/TeleChat2";
    }
}