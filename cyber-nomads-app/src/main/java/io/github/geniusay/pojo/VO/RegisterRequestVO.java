package io.github.geniusay.pojo.VO;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 22:43
 */
@Data
@Builder
public class RegisterRequestVO {

    private String email;
    private String password;
    private String code;
}
