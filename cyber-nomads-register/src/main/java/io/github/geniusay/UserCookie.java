package io.github.geniusay;

import lombok.Builder;
import lombok.Data;
import org.openqa.selenium.Cookie;

import java.util.HashSet;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 13:40
 */
@Data
@Builder
public class UserCookie {

    private String username;
    private String password;
    private HashSet<Cookie> cookie;

}
