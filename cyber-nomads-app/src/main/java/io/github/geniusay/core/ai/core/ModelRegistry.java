package io.github.geniusay.core.ai.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述: 模型注册中心
 * @author suifeng
 * 日期: 2025/3/3
 */
@Component
public class ModelRegistry {
    private final Map<String, AIModel<?>> models = new ConcurrentHashMap<>();

    @Autowired
    public ModelRegistry(List<AIModel<?>> modelList) {
        modelList.forEach(model -> {
            String modelName = model.getName();
            models.put(modelName, model);
            System.out.println("Registered AI Model: " + modelName);
        });
    }

    public AIModel<?> getModel(String modelType) {
        return Optional.ofNullable(models.get(modelType))
                .orElseThrow(() -> new IllegalArgumentException("Unregistered model type: " + modelType));
    }
}

