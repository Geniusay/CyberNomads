package io.github.geniusay.pojo.VO;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:55
 */
@Data
@Builder
public class LoginVO {

    UserVO userVO;
    String token;

}
