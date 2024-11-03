package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class UserCookie {

    private String username;
    private List<UCookie> cookie;

}
