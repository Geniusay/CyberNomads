package io.github.geniusay.core.ai.delegate;

import io.github.geniusay.core.ai.core.AIModel;
import io.github.geniusay.core.ai.core.AIOperation;
import io.github.geniusay.core.ai.core.ModelRegistry;
import io.github.geniusay.core.ai.core.OperationRegistry;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: AI操作顶级接口
 * @author suifeng
 * 日期: 2025/3/3
 */
@RequiredArgsConstructor
@Service
public class AIService {

    private final OperationRegistry operationRegistry;
    private final ModelRegistry modelRegistry;

    public <P, R> R execute(String operationType, String modelType, P params) {
        @SuppressWarnings("unchecked")
        AIOperation<P, R> operation = (AIOperation<P, R>) operationRegistry.getOperation(operationType);
        
        // 校验模型是否被支持
        if (!operation.supportedModels().contains(modelType)) {
            throw new IllegalArgumentException("操作["+operationType+"]不支持模型: " + modelType);
        }
        
        AIModel model = modelRegistry.getModel(modelType);
        String prompt = operation.buildPrompt(params);
        String response = model.generate(prompt);
        
        return operation.parseResponse(response);
    }

    // 包装模型参数
    public List<TaskNeedParams> getTaskNeedParams(List<String> aiModels) {
        List<TaskNeedParams> taskNeedParams = new ArrayList<>();
        for (String aiModel : aiModels) {
            AIModel model = modelRegistry.getModel(aiModel);
            TaskNeedParams taskNeedParam = TaskNeedParams.ofK(model.getName(), String.class, model.description());
            taskNeedParams.add(taskNeedParam);
        }
        return taskNeedParams;
    }
}
