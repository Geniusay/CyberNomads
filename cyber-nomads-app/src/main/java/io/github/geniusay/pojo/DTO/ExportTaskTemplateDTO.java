package io.github.geniusay.pojo.DTO;

import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/20 10:44
 */
@Data
@Validated
public class ExportTaskTemplateDTO {
    @NotBlank(message = "模板名称不能为空")
    private String name;
    @NotBlank(message = "模板描述不能为空")
    private String description;
    private Boolean hasPrivate;
    private String taskId;
}
