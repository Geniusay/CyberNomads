package io.github.geniusay.core.ai.core;

import lombok.Data;

/**
 * 描述: 模型配置抽象类
 * @author suifeng
 * 日期: 2025/3/3
 */
@Data
public abstract class ModelConfig {

    private String baseUrl;

    private String apiKey;
}
