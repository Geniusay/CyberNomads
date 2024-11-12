package io.github.geniusay.pojo.DTO;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 19:12
 */
@Data
@Validated
public class LoginDTO {
    @NotBlank(message = "账号不能为空")
    private String username;
    private String password;
    @NotBlank(message = "平台不能为空")
    private String platform;
    @NotBlank(message = "驱动路径不能为空")
    private String driverPath;
    @NotBlank(message = "浏览器路径不能为空")
    private String browserPath;
}
