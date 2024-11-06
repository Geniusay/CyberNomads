package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.pojo.DTO.CreateTaskDTO;
import io.github.geniusay.pojo.DTO.ModifyTaskDTO;
import io.github.geniusay.pojo.DTO.UpdateRobotsDTO;
import io.github.geniusay.pojo.DTO.UpdateTaskDTO;
import io.github.geniusay.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    /**
     * 创建任务并返回任务详情
     */
    @TokenRequire
    @PostMapping("/create")
    public Result<?> createTask(@RequestBody CreateTaskDTO create) {
        return Result.success(taskService.createTask(create));
    }

    /**
     * 根据用户 uid 获取所有任务
     */
    @TokenRequire
    @GetMapping("/list")
    public Result<?> getUserTasks(@RequestParam String uid) {
        return Result.success(taskService.getUserTasks(uid));
    }

    /**
     * 更新任务的所有字段（除任务状态外），并返回更新后的任务详情
     */
    @TokenRequire
    @PostMapping("/update")
    public Result<?> updateTask(@RequestBody UpdateTaskDTO updateTaskDTO) {
        return Result.success(taskService.updateTask(updateTaskDTO));
    }

    /**
     * 修改任务状态
     * 支持开始任务，删除和重置操作：
     */
    @TokenRequire
    @PostMapping("/changeStatus")
    public Result<?> modifyTask(@RequestBody ModifyTaskDTO modifyTaskDTO) {
        taskService.modifyTask(modifyTaskDTO);
        return Result.success();
    }
}