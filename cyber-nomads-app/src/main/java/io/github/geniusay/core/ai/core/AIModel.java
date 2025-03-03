package io.github.geniusay.core.ai.core;

/**
 * 描述: 模型通用接口
 * @author suifeng
 * 日期: 2025/3/3
 */
public interface AIModel<C extends ModelConfig> {

    String getName();

    String description();

    void init(C config);

    String generate(String prompt);

    <T> T generate(String prompt, Class<T> responseType);
}
