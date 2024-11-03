package io.github.geniusay.service;

import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.TaskFunctionDTO;
import io.github.geniusay.pojo.VO.TaskVO;

import java.util.List;
import java.util.Map;

public interface TaskService {

    /**
     * 创建任务并返回任务详情
     */
    TaskVO createTask(String taskName, String platform, String taskType, Map<String, Object> params, List<Long> robotIds);

    /**
     * 获取支持的平台列表
     */
    List<Map<String, String>> getSupportedPlatforms();

    /**
     * 获取指定平台的任务功能及其参数
     */
    List<TaskFunctionDTO> getFunctionsAndParamsByPlatform(String platform);

    /**
     * 获取指定用户的所有任务
     */
    List<TaskVO> getUserTasks(String uid);

    /**
     * 批量添加或删除机器人账号，并返回更新后的任务详情
     */
    TaskVO updateRobotsInTask(Long taskId, List<Long> robotIds, boolean isAdd);

    /**
     * 更新任务的 params 参数，并返回更新后的任务详情
     */
    TaskVO updateTaskParams(Long taskId, Map<String, Object> params);

    /**
     * 修改任务状态，支持删除和重置操作
     */
    void modifyTask(Long taskId, String action);

    /**
     * 修改任务状态，供内部调度器调用
     */
    void changeTaskStatus(Long taskId, String newStatus);

    /**
     * 批量解析任务中的 robots 字段，并填充 robotList 字段
     */
    List<TaskDO> populateRobotListForTasks(List<TaskDO> taskDOList, RobotMapper robotMapper);
}