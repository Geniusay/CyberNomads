package io.github.geniusay.core.ai.config;

import io.github.geniusay.core.ai.core.ModelConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "ai.models.siliconflow")
@Component
@Data
public class SiliconflowConfig extends ModelConfig {

    private String version = "deepseek-ai/DeepSeek-V3";

    private Double temperature = 0.7;

    private Integer maxTokens = 2048;
}