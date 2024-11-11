package io.github.geniusay.service;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:15
 */
public interface UserService {

    //验证令牌合法性
    String verifyTokenLegitimacy(String code);


}
