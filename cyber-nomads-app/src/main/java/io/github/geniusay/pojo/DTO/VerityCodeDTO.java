package io.github.geniusay.pojo.DTO;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 15:08
 */
@Data
@Validated
public class VerityCodeDTO {

    @NotBlank(message = "email不能为空")
    @Email
    private String email;
    @NotBlank(message = "令牌不能为空")
    private String code;

}
