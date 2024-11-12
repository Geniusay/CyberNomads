package io.github.geniusay.pojo.DTO;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 21:25
 */
@Builder
@Data
public class LoginMachineDTO {

    private String username;
    private String cookie;
    private String password;
    private String platform;
}
