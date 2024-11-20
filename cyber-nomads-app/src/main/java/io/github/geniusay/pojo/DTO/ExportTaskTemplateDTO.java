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

    private String id;
    @NotBlank(message = "模板名称不能为空")
    private String name;
    @NotBlank(message = "模板描述不能为空")
    private String description;
    @NotBlank(message = "模板类型不能为空")
    private String type;
    @NotBlank(message = "模板平台不能为空")
    private String platform;
    private String params;
    private Boolean hasPrivate;


}
