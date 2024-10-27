package io.github.geniusay.pojo.VO;

import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:57
 */
@Data
public class LoginRequestVO {

    private String pid;
    private String code;
    private String pinId;
    private String email;
    private String password;

}
