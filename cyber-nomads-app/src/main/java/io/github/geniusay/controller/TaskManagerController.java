package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.service.TaskManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description 调度器控制面板，获取当前robot已经对应worker
 * @Author welsir
 * @Date 2024/11/24 20:28
 */
@RequestMapping("/control/scheduler")
@RestController
public class TaskManagerController {

    @Resource
    TaskManagerService service;
    @GetMapping("/queryWorkerTask")
    public Result<?> getRobot(@RequestParam(required = false,value = "id") String workerId){
        return Result.success(service.queryWorkerTasks(workerId));
    }

    @GetMapping("/queryTask")
    public Result<?> getTask(@RequestParam(required = false,value = "id") String taskId){
        return Result.success(service.queryTask(taskId));
    }
}
