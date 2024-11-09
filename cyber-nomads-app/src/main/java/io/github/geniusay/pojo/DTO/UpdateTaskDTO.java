package io.github.geniusay.pojo.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Validated
public class UpdateTaskDTO {

    @NotNull(message = "taskId不能为空")
    private Long taskId;  // 任务ID，必须传递

    private String taskName;  // 可选，任务名称

    private String platform;  // 可选，平台

    private String taskType;  // 可选，任务类型

    private Map<String, Object> params;  // 可选，任务参数

    private List<Long> robotIds;  // 可选，机器人ID列表
}
