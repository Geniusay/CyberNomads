package io.github.geniusay.pojo.DTO;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:50
 */
@Data
@Builder
public class UserDTO {

    private Long id;
    private Long uid;
    private String avator;
    private String email;
    private String password;
    private String nickName;
    private String phoneNum;
    private Integer point;

}
