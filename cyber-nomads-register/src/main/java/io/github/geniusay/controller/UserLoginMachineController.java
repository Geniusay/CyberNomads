package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.pojo.DTO.DriverPathDTO;
import io.github.geniusay.pojo.DTO.LoginDTO;
import io.github.geniusay.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
        return userService.queryRobots();
    }

    @PostMapping("/login")
    public Result<?> loginRobot(@RequestBody @Validated LoginDTO loginDTO){
        return Result.success(userService.login(loginDTO));
    }

    @PostMapping("/exit")
    public void exit(){
        System.exit(0);
    }

    @GetMapping("/verityCode")
    public Result<?> checkCodeValid(@RequestParam String code){
        return Result.success(userService.verityCode(code));
    }

    @PostMapping("/addDriverPath")
    public Result<?> pushPathToServer(@RequestBody @Validated DriverPathDTO driverPathDTO){
        return Result.success(userService.verityPath(driverPathDTO));
    }

    @GetMapping("/getPath")
    public Result<?> queryPathIfExist(){
        return Result.success(userService.queryPathExist());
    }
}
