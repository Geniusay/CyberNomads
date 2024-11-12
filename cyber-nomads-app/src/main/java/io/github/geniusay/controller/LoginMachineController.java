package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.core.anno.LoginMachineToken;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.service.LoginMachineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:53
 */
@RestController
@RequestMapping("/loginMachine")
public class LoginMachineController {

    @Resource
    LoginMachineService service;

    @PostMapping("/code")
    @TokenRequire
    public Result<?> generateCode(){
        return Result.success(service.generateCode());
    }

    @GetMapping("/queryMachineInfo")
    @TokenRequire
    public Result<?> queryMachineInfo(int id){
        return Result.success(service.queryMachineInfo(id));
    }

    @PostMapping("/logout")
    @TokenRequire
    @LoginMachineToken
    public Result<?> loginMachineLogout(String scriptCode){
        return Result.success(service.logout(scriptCode));
    }

}
