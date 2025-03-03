package io.github.geniusay.core.ai.operate;

import io.github.geniusay.core.ai.core.AIOp;
import io.github.geniusay.core.ai.core.AIOperation;
import io.github.geniusay.core.supertask.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.AIConstant.QW_MODEL;

@AIOp("generate_entity")
@Component
public class GenerateEntityOperation implements AIOperation<Map<String, Object>, List<Task>> {

    @Override
    public List<String> supportedModels() {
        return List.of(QW_MODEL, "gpt4");
    }

    @Override
    public String buildPrompt(Map<String, Object> projectOutline) {
        return "Generate Entity";
    }

    @Override
    public List<Task> parseResponse(String aiResponse) {
        return null;
    }
}
