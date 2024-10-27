package io.github.geniusay.controller;
import io.github.common.web.Result;

import io.github.geniusay.async.AsyncService;
import io.github.geniusay.pojo.VO.LoginRequestVO;
import io.github.geniusay.pojo.VO.RegisterRequestVO;
import io.github.geniusay.service.UserService;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.ImageUtil;
import io.github.geniusay.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 21:34
 */

@RestController("/user")
@ResponseBody
@Validated
public class UserController {

    private ImageUtil imageUtil;
    private UserService userService;
    private AsyncService asyncService;
    @Autowired
    public UserController(ImageUtil imageUtil,UserService userService,AsyncService asyncService){
        this.imageUtil = imageUtil;
        this.userService = userService;
        this.asyncService = asyncService;
    }

    @PostMapping("captcha")
    public Result<?> queryCaptcha(){
        String pid = RandomUtil.generateRandomString(6);
        Map<String, String> code = imageUtil.generateCode();
        CacheUtil.putCaptcha(pid,code.get("code"));
        code.put("pid",pid);
        return Result.success(code);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody
                               @Valid
            LoginRequestVO req){
        return Result.success(userService.login(req));
    }

    @PostMapping("register")
    public Result<?> register(RegisterRequestVO req){
        return Result.success(userService.register(req));
    }

    @GetMapping("search/{uid}")
    public Result<?> queryUser(@PathVariable Integer uid){
        return Result.success(userService.queryUserById(uid));
    }

    @GetMapping("search")
    public Result<?> queryUser(){
        return Result.success(userService.queryUser());
    }

    @PostMapping("register/sendCaptcha")
    public Result<?> preRegister(@RequestParam
                                     @Valid
                                     @NotNull(message = "邮箱不能为空")
                                     String email){
        asyncService.sendCodeToEmail(email,RandomUtil.generateRandomString(4));
        return Result.success();
    }

}
