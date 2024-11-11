package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.pojo.DTO.VerityCodeDTO;
import io.github.geniusay.service.LoginMachineService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/verity")
    public Result<?> verityCode(@RequestBody
                                    @Validated VerityCodeDTO verityCodeDTO){
        return Result.success(service.verity(verityCodeDTO));
    }

    @PostMapping("/code")
    @TokenRequire
    public Result<?> generateCode(){
        return Result.success(service.generateCode());
    }

}
