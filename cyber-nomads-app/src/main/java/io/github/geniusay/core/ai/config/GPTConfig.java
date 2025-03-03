package io.github.geniusay.core.ai.config;

import io.github.geniusay.core.ai.core.ModelConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述: GPT模型配置信息
 * @author suifeng
 * 日期: 2025/3/3
 */
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "ai.models.gpt")
@Component
@Data
public class GPTConfig extends ModelConfig {

    // 模型版本
    private String version = "gpt-4o";
}
