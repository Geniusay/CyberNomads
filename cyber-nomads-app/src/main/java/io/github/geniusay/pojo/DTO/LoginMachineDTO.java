package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/12 1:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginMachineDTO {
    private String username;
    private String password;
    private String platform;
    private String cookie;
}
