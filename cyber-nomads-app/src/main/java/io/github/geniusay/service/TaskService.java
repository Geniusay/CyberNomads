package io.github.geniusay.service;

import io.github.geniusay.pojo.DTO.TaskFunctionDTO;
import io.github.geniusay.pojo.VO.TaskVO;

import java.util.List;
import java.util.Map;

public interface TaskService {

    /**
     * 创建任务并返回任务详情
     */
    TaskVO createTask(String taskName, String platform, String taskType, Map<String, Object> params);

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
     * 批量添加或删除机器人账号
     */
    void updateRobotsInTask(Long taskId, List<Long> robotIds, boolean isAdd);

    /**
     * 更新任务的 params 参数，并返回更新后的任务详情
     */
    TaskVO updateTaskParams(Long taskId, Map<String, Object> params);
}