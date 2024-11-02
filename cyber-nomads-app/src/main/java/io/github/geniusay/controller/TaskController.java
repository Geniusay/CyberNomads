package io.github.geniusay.controller;

import io.github.geniusay.core.anno.TokenRequire;
import io.github.geniusay.core.exception.R;
import io.github.geniusay.pojo.DTO.CreatTaskDTO;
import io.github.geniusay.pojo.DTO.UpdateRobotsDTO;
import io.github.geniusay.pojo.VO.TaskVO;
import io.github.geniusay.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    public R<TaskVO> createTask(@RequestBody CreatTaskDTO creat) {
        TaskVO taskVO = taskService.createTask(creat.getTaskName(), creat.getPlatform(), creat.getTaskType(), creat.getParams());
        return R.success(taskVO);
    }

    /**
     * 根据用户 uid 获取所有任务
     */

    @TokenRequire
    @GetMapping("/user/{uid}")
    public R<List<TaskVO>> getUserTasks(@PathVariable String uid) {
        return R.success(taskService.getUserTasks(uid));
    }

    /**
     * 批量添加或删除机器人账号
     */
    @TokenRequire
    @PostMapping("/robots/update")
    public R<String> updateRobotsInTask(@RequestBody UpdateRobotsDTO updateRobotsDTO) {
        taskService.updateRobotsInTask(updateRobotsDTO.getTaskId(), updateRobotsDTO.getRobotIds(), updateRobotsDTO.isHasAdd());
        return R.success("操作成功");
    }

    /**
     * 修改任务的 params 参数，并返回更新后的任务详情
     */
    @TokenRequire
    @PostMapping("/{taskId}/params/update")
    public R<TaskVO> updateTaskParams(@PathVariable Long taskId, @RequestBody Map<String, Object> params) {
        TaskVO updatedTask = taskService.updateTaskParams(taskId, params);
        return R.success(updatedTask);
    }
}