package io.github.geniusay.pojo.DTO;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:57
 */
@Data
@Builder
@Validated
public class LoginRequestDTO {
    @Length(min = 4, max = 10, message = "验证码长度必须在4到6之间")
    @NotNull(message = "code不能为null")
    private String code;
    @NotNull(message = "邮箱不能为空")
    @Length(min = 6, max = 30, message = "邮箱长度必须在6到30之间")
    @Email( message = "参数必须为邮箱")
    private String email;

}
