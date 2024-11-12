package io.github.geniusay.strategy.login;

import io.github.geniusay.pojo.DTO.LoginDTO;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 20:41
 */
public interface LoginStrategy {

    Boolean login(LoginDTO loginDTO);

    String platform();
}
