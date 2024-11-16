package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/15 20:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverPathDTO {

    @NotBlank(message = "浏览器路径不能为空")
    private String browserPath;
    @NotBlank(message = "驱动路径不能为空")
    private String driverPath;

}
