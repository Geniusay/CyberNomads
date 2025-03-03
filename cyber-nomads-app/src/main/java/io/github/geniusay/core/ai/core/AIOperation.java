package io.github.geniusay.core.ai.core;

/**
 * 描述: AI操作接口
 * @author suifeng
 * 日期: 2025/3/3
 */
public interface AIOperation<P, R> {

    String buildPrompt(P params);

    R parseResponse(String aiResponse);

    default void validate(P params) {}
}
