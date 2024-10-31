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

    @TokenRequire
    @GetMapping("/search")
    public Result<?> queryUser(@RequestParam("uid") String uid){
        return Result.success(userService.queryUserById(uid));
    }

    @TokenRequire
    @GetMapping("/getInfo")
    public Result<?> getUserInfo(){
        return userService.getUserInfo();
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

    @PostMapping("/robot/upload")
    @TokenRequire
    public Result<?> uploadRobot(@RequestParam("file") MultipartFile file){
        return Result.success(userService.loadRobot(file));
    }

    @GetMapping("/robot/search")
    @TokenRequire
    public Result<?> getUserRobot(){
        return Result.success(userService.queryRobot());
    }

    @DeleteMapping("/robot")
    @TokenRequire
    public Result<?> removeRobot(@RequestParam("id") Long id){
        return Result.success(userService.removeRoobot(id));
    }

    @TokenRequire
    @PostMapping("/robot/ban")
    public Result<?> banRobot(@RequestParam("id") Long id){
        return Result.success(userService.banRoobot(id));
    }

    @TokenRequire
    @GetMapping("/getPoint")
    public Result<?> getPoint(){
        return Result.success(userPointUtil.get());
    }
}
