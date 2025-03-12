package io.github.geniusay.core.ai.model.siliconflow;

import io.github.geniusay.core.ai.config.SiliconflowConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.THUDM;

@Component
public class Thudm extends AbstractSiliconflowModel {

    public Thudm(SiliconflowConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return THUDM;
    }

    @Override
    public String description() {
        return "智谱清言 (glm-4) ";
    }

    @Override
    protected String getModelVersion() {
        return "THUDM/glm-4-9b-chat";
    }
}