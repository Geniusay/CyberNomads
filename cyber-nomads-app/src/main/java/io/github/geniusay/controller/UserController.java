package io.github.geniusay.controller;
import io.github.common.web.Result;

import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.pojo.DTO.LoginRequestDTO;
import io.github.geniusay.pojo.DTO.RegisterRequestDTO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.utils.UserPointUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:34
 */

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserPointUtil userPointUtil;

    @PostMapping("/captcha")
    public Result<?> queryCaptcha(){
        return Result.success(userService.generateCaptcha());
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody
                               @Valid
                           LoginRequestDTO req){
        return Result.success(userService.login(req));
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody
                                @Valid
                              RegisterRequestDTO req){
        return Result.success(userService.register(req));
    }

    @PostMapping("/logout")
    public Result<?> logout(){
        userService.logout();
        return Result.success();
    }

    @GetMapping("/getInfo")
    @TokenRequire
    public Result<?> getUserInfo(){
        return userService.getUserInfo();
    }

    @TokenRequire
    public Result<?> queryUser(@PathVariable String uid){
        return Result.success(userService.queryUserById(uid));
    }

    @PostMapping("/sendCaptcha")
    public Result<?> preEmail(        @Valid
                                      @NotNull(message = "邮箱不能为空")
                                      String email,
                                      @Valid
                                      @NotNull(message = "pid不能为空")
                                      String pid,
                                      @Valid
                                      @NotNull(message = "验证码不能为空")
                                      String code){
        userService.generateEmailCode(email,pid,code);
        return Result.success();
    }

    @TokenRequire
    @GetMapping("/getPoint")
    public Result<?> getPoint(){
        return Result.success(userPointUtil.get());
    }
}
