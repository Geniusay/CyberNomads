package io.github.geniusay.core.ai.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "ai")
@Component
@Data
public class AiProperties {
    private Map<String, ModelConfig> models = new HashMap<>();
}
