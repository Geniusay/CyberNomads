package io.github.geniusay.pojo.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/3 17:24
 */
@Data
@Builder
public class UserCookieDTO {

    List<UserCookie> userCookies;

}
