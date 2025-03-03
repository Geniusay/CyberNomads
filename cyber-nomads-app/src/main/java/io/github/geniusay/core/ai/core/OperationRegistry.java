package io.github.geniusay.core.ai.core;

import io.github.geniusay.core.exception.ServeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述: AI操作注册中心
 * @author suifeng
 * 日期: 2025/3/3
 */
@Component
public class OperationRegistry {

    private final Map<String, AIOperation<?, ?>> operationMap = new ConcurrentHashMap<>();

    @Autowired
    public OperationRegistry(List<AIOperation<?, ?>> operations) {
        operations.forEach(op -> {
            String operationName = op.getClass().getAnnotation(AIOp.class).value();
            operationMap.put(operationName, op);
        });
    }

    public AIOperation<?, ?> getOperation(String name) {
        return Optional.ofNullable(operationMap.get(name)).orElseThrow(() -> new ServeException("Operation not found: " + name));
    }
}
