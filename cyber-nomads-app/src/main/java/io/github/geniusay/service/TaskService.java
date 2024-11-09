package io.github.geniusay.service;

import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.VO.TaskVO;

import java.util.List;
import java.util.Map;

public interface TaskService {

    /**
     * 创建任务并返回任务详情
     */
    TaskVO createTask(CreateTaskDTO create);


    /**
     * 获取指定平台的任务功能及其参数
     */
    List<TaskFunctionDTO> getFunctionsAndParamsByPlatform(String platform);

    /**
     * 获取指定用户的所有任务
     */
    List<TaskVO> getUserTasks();

    /**
     * 更新任务的所有字段（除任务状态外），并返回更新后的任务详情
     */
    TaskVO updateTask(UpdateTaskDTO updateTaskDTO);

    /**
     * 修改任务状态，支持删除和重置操作
     */
    void modifyTask(ModifyTaskDTO modifyTaskDTO);

    /**
     * 批量解析任务中的 robots 字段，并填充 robotList 字段
     */
    List<TaskDO> populateRobotListForTasks(List<TaskDO> taskDOList);

    /*
     * 获取指定状态的任务(内部)
     */
    List<TaskDO> getTaskByStatus(List<String> status);

    /**
     * 删除任务
     */
    void deleteTask(Long taskId);
}
