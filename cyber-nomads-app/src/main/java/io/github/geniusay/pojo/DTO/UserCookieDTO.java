package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.Cookie;

import java.util.HashSet;
import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 13:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCookieDTO {

    private String username;
    private String password;
    private List<UCookie> cookie;

}
