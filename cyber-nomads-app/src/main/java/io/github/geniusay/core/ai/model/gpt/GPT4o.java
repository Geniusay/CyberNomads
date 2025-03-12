package io.github.geniusay.core.ai.model.gpt;

import io.github.geniusay.core.ai.config.GPTConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.GPT_4O;

/**
 * 描述: GPT模型 (gpt-4o)
 * @author suifeng
 * 日期: 2025/3/3
 */
@Component
public class GPT4o extends AbstractGPTModel {

    public GPT4o(GPTConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return GPT_4O;
    }

    @Override
    public String description() {
        return "GPT-4o";
    }

    @Override
    protected String getVersion() {
        return "gpt-4o";
    }
}
