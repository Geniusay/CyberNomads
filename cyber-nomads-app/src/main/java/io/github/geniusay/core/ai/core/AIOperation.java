package io.github.geniusay.core.ai.core;

import java.util.List;

/**
 * 描述: AI操作接口
 * @author suifeng
 * 日期: 2025/3/3
 */
public interface AIOperation<P, R> {

    // 支持的模型列表
    List<String> supportedModels();

    // 根据参数生成提示词
    String buildPrompt(P params);

    // 解析响应
    R parseResponse(String aiResponse);

    default void validate(P params) {}
}
