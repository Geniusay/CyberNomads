package io.github.geniusay.core.ai.model;

import io.github.geniusay.core.ai.config.GPTConfig;
import io.github.geniusay.core.ai.core.AIModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.GPT_MODEL;

/**
 * 描述: GPT模型
 * @author suifeng
 * 日期: 2025/3/3
 */
@RequiredArgsConstructor
@Component
public class GPTModel implements AIModel {

    private final GPTConfig config;

    @Override
    public String getName() {
        return GPT_MODEL;
    }

    @Override
    public String description() {
        return "GPT";
    }

    @Override
    public String generate(String prompt) {
       return "GTP生成内容";
    }

    @Override
    public <T> T generate(String prompt, Class<T> responseType) {
        return null;
    }
}
