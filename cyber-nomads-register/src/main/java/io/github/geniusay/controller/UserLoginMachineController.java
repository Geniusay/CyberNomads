package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.LoginDTO;
import io.github.geniusay.pojo.DTO.VerityDTO;
import io.github.geniusay.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:05
 */
@RestController
@RequestMapping("/loginMachine")
@Validated
public class UserLoginMachineController {

    @Resource
    UserService userService;

    @GetMapping("/getRobots")
    public Result<?> getRobots(){
        return Result.success(userService.queryRobots());
    }

    @PostMapping("/login")
    public Result<?> loginRobot(@RequestBody @Validated LoginDTO loginDTO){
        return Result.success(userService.login(loginDTO));
    }

    @PostMapping("/saveKey")
    public Result<?> saveScriptKey(@RequestBody String scriptKey){
        userService.saveKey(scriptKey);
        return Result.success();
    }

    @PostMapping("/exit")
    public void exit(){
        System.exit(0);
    }
}
