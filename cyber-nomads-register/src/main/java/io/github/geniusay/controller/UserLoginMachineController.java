package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:05
 */
@RestController
@RequestMapping("/loginMachine")
public class UserLoginMachineController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result<?> login(String code){
        return Result.success(userService.verifyTokenLegitimacy(code));
    }

}
