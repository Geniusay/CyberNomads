package io.github.geniusay.service;

import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.TaskFunctionDTO;

import java.util.List;
import java.util.Map;

public interface ITaskService {

    /**
     * 创建任务并保存到数据库
     */
    void createTask(String taskName, String platform, String taskType, Map<String, Object> params);

    /**
     * 获取支持的平台列表
     * @return 支持的平台列表
     */
    List<Map<String, String>> getSupportedPlatforms();

    /**
     * 获取指定平台的任务功能及其参数
     */
    List<TaskFunctionDTO> getFunctionsAndParamsByPlatform(String platform);

    /**
     * 获取指定用户的所有任务
     */
    List<TaskDO> getUserTasks(String uid);

    /**
     * 批量添加或删除机器人账号
     */
    void updateRobotsInTask(Long taskId, List<Long> robotIds, boolean isAdd);

    /**
     * 更新任务的 params 参数
     */
    void updateTaskParams(Long taskId, Map<String, Object> params);
}