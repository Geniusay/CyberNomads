package io.github.geniusay.core.ai.config;

import io.github.geniusay.core.ai.core.ModelConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述: 千问模型配置信息
 * @author suifeng
 * 日期: 2025/3/3
 */
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "ai.models.qw")
@Component
@Data
public class QwConfig extends ModelConfig {

    // 系统提示词
    private String systemPrompt = "You are an expert in java back-end programming";
}
