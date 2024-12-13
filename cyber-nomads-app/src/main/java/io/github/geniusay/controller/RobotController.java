package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.core.anno.LoginMachineToken;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.service.RobotService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/robot")
public class RobotController {

    @Resource
    private RobotService robotService;

    @PostMapping("/upload")
    @TokenRequire
    public Result<?> uploadRobot(@RequestParam("file") MultipartFile file){
        return Result.success(robotService.loadRobot(file));
    }

    @GetMapping("/search")
    @LoginMachineToken
    @TokenRequire
    public Result<?> getUserRobot(){
        return Result.success(robotService.queryRobot());
    }

    @GetMapping("/delete")
    @TokenRequire
    public Result<?> removeRobot(@RequestParam("id") Long id){
        return Result.success(robotService.removeRoobot(id));
    }

    @GetMapping("/ban")
    @TokenRequire
    public Result<?> banRobot(@RequestParam("id") Long id){
        return Result.success(robotService.banRoobot(id));
    }

    @PostMapping("/change")
    @TokenRequire
    public Result<?> changeRobot(@RequestBody ChangeRobotDTO robotDTO){
        return Result.success(robotService.changeRobot(robotDTO));
    }

    @PostMapping("/add")
    @TokenRequire
    public Result<?> addRobot(@RequestBody AddRobotDTO robotDTO){
        return Result.success(robotService.addRobot(robotDTO));
    }

    @PostMapping("/addQr")
    @TokenRequire
    public Result<?> addRobotQr(@RequestBody AddRobotDTO robotDTO){
        return Result.success(robotService.addRobotQr(robotDTO));
    }

    @PostMapping("/getCookie")
    @TokenRequire
    public Result<?> getCookie(@RequestBody @Valid GetCookieDTO getCookieDTO){
        return Result.success(robotService.getCookie(getCookieDTO));
    }

    @PostMapping("/changeCookie")
    @TokenRequire
    public Result<?> changeCookie(@RequestBody @Valid UpdateCookieDTO updateCookieDTO){
        return Result.success(robotService.changeRobotCookie(updateCookieDTO));
    }

    @PostMapping("/insertOrUpdate")
    @TokenRequire
    @LoginMachineToken
    public Result<?> insertOrUpdate(@RequestBody @Validated LoginMachineDTO loginMachineDTO){
        return Result.success(robotService.insertOrUpdateRobot(loginMachineDTO));
    }

    @PostMapping("/share")
    @TokenRequire
    public Result<?> shareRobot(@RequestBody @Validated ShareRobotDTO shareRobotDTO){
        return Result.success(robotService.shareRobot(shareRobotDTO));
    }

    @GetMapping("/sharedRobotPage")
    @TokenRequire
    public Result<?> getPage(@RequestParam Integer page, @RequestParam String taskType){
        return Result.success(robotService.sharedRobotPage(page, taskType));
    }

    @GetMapping("/sharedRobotInfo")
    @TokenRequire
    public Result<?> getSharedRobot(@RequestParam("id") Long id){
        return Result.success(robotService.sharedRobotInfo(id));
    }

    // TODO: 推荐算法
    @GetMapping("/recommend")
    @TokenRequire
    public Result<?> recommend(@RequestParam("page") Integer page){
        return Result.success(robotService.recommend(page));
    }
}
