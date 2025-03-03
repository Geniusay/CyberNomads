package io.github.geniusay.core.ai.model.gpt;

import io.github.geniusay.core.ai.config.GPTConfig;
import io.github.geniusay.core.ai.core.AIModel;
import lombok.RequiredArgsConstructor;

/**
 * 描述: GPT模型
 * @author suifeng
 * 日期: 2025/3/3
 */
@RequiredArgsConstructor
public abstract class AbstractGPTModel implements AIModel {

    private final GPTConfig config;

    @Override
    public String generate(String prompt) {
       return "GTP生成内容";
    }

    @Override
    public <T> T generate(String prompt, Class<T> responseType) {
        return null;
    }

    protected abstract String getVersion();
}
