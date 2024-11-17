package io.github.geniusay.pojo.DTO;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CreateTaskDTO {

    @NotBlank(message = "任务名称不能为空")
    @Length(max = 20, message = "任务名称不能超过20个字符")
    private String taskName;

    @NotBlank(message = "平台不能为空")
    @Length(max = 20, message = "平台名称不能超过20个字符")
    private String platform;

    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    private Map<String, Object> params;

    private List<Long> robotIds;
}
