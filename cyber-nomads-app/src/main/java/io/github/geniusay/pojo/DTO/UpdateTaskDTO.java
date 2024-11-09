package io.github.geniusay.pojo.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class UpdateTaskDTO {

    @NotNull(message = "任务ID不能为空")
    private Long taskId;  // 任务ID，必须传递

    @Length(max = 20, message = "任务名称不能超过20个字符")
    private String taskName;

    @Length(max = 20, message = "平台名称不能超过20个字符")
    private String platform;

    @Length(max = 20, message = "任务类型不能超过20个字符")
    private String taskType;

    private Map<String, Object> params;

    private List<Long> robotIds;
}