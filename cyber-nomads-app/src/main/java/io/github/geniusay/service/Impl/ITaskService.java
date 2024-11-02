package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.core.supertask.TaskParamValidator;
import io.github.geniusay.core.supertask.TaskStrategyManager;
import io.github.geniusay.core.supertask.config.TaskPlatformConstant;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.config.TaskTypeConstant;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.TaskFunctionDTO;
import io.github.geniusay.pojo.VO.TaskVO;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.utils.ConvertorUtil;
import io.github.geniusay.utils.TaskTranslationUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.geniusay.utils.AIGenerate.ValidUtil.isValidConstant;

@Service
public class ITaskService implements TaskService {

    @Resource
    private TaskStrategyManager taskStrategyManager;

    @Resource
    private TaskMapper taskMapper;

    @Override
    public void createTask(String taskName, String platform, String taskType, Map<String, Object> params) {
        // 1. 从线程中获取 uid 和 nickname
        String uid = ThreadUtil.getUid();
        String nickname = ThreadUtil.getUsername();

        // 2. 校验 platform 和 taskType 是否有效
        validatePlatformAndTaskType(platform, taskType);

        // 3. 通过 taskStrategyManager 直接获取任务蓝图
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(platform, taskType);
        if (blueprint == null) {
            throw new IllegalArgumentException("任务类型不支持: " + taskType);
        }

        // 4. 获取任务的必需参数
        List<TaskNeedParams> needParams = blueprint.supplierNeedParams();

        // 5. 验证前端传递的参数
        try {
            TaskParamValidator.validateParams(needParams, params);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        // 6. 初始化 TaskDO 对象
        TaskDO taskDO = new TaskDO();
        taskDO.setUid(uid);
        taskDO.setNickname(nickname);
        taskDO.setTaskName(taskName);
        taskDO.setPlatform(platform);
        taskDO.setTaskType(taskType);
        taskDO.setTaskStatus(TaskStatus.PENDING);
        taskDO.setRobots(ConvertorUtil.listToString(List.of()));
        taskDO.setParams(ConvertorUtil.mapToJsonString(params));

        // 7. 保存任务到数据库
        try {
            taskMapper.insert(taskDO);
        } catch (Exception e) {
            throw new RuntimeException("任务保存失败", e);
        }
    }

    private void validatePlatformAndTaskType(String platform, String taskType) {
        if (!isValidConstant(TaskPlatformConstant.class, platform)) {
            throw new IllegalArgumentException("不支持的平台: " + platform);
        }

        if (!isValidConstant(TaskTypeConstant.class, taskType)) {
            throw new IllegalArgumentException("不支持的任务类型: " + taskType);
        }
    }

    @Override
    public List<TaskVO> getUserTasks(String uid) {
        // 查询任务列表
        QueryWrapper<TaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        List<TaskDO> taskDOList = taskMapper.selectList(queryWrapper);

        // 将 TaskDO 转换为 TaskVO，并查询 robots 列表
        return taskDOList.stream().map(taskDO -> {
            // 将 params 字段从 JSON 字符串转换为 Map
            Map<String, Object> params = ConvertorUtil.jsonStringToMap(taskDO.getParams());

            // 将 robots 字段从逗号分隔的字符串转换为 List<Long>
            List<Long> robotIds = ConvertorUtil.stringToList(taskDO.getRobots());

            // 封装为 TaskVO
            return new TaskVO(
                    taskDO.getId(),
                    taskDO.getUid(),
                    taskDO.getNickname(),
                    taskDO.getTaskName(),
                    taskDO.getPlatform(),
                    taskDO.getTaskType(),
                    taskDO.getTaskStatus().name(),
                    robotIds,
                    params
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void updateRobotsInTask(Long taskId, List<Long> robotIds, boolean isAdd) {
        // 获取任务
        TaskDO task = taskMapper.selectById(taskId);
        List<Long> robots = null;
        if (task != null) {
            robots = ConvertorUtil.stringToList(task.getRobots());
            if (robots == null) {
                robots = new ArrayList<>();
            }
            if (isAdd) {
                // 批量添加机器人
                for (Long robotId : robotIds) {
                    if (!robots.contains(robotId)) {
                        robots.add(robotId);
                    }
                }
            } else {
                // 批量删除机器人
                robots.removeAll(robotIds);
            }
            task.setRobots(ConvertorUtil.listToString(robots));
            // 更新任务
            taskMapper.updateById(task);
        }
    }

    @Override
    public void updateTaskParams(Long taskId, Map<String, Object> params) {
        // 获取任务
        TaskDO task = taskMapper.selectById(taskId);
        if (task != null) {
            // 更新 params 字段
            task.setParams(ConvertorUtil.mapToJsonString(params));
            taskMapper.updateById(task);  // 更新任务
        }
    }

    @Override
    public List<Map<String, String>> getSupportedPlatforms() {
        return taskStrategyManager.getSupportedPlatforms().stream()
                .map(platform -> Map.of(
                        "key", platform, // 英文平台名称
                        "value", TaskTranslationUtil.translatePlatform(platform) // 中文平台名称
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskFunctionDTO> getFunctionsAndParamsByPlatform(String platform) {
        return taskStrategyManager.getBlueprintsForPlatform(platform).stream()
                .map(blueprint -> new TaskFunctionDTO(
                        blueprint.taskType(), // 英文任务类型
                        TaskTranslationUtil.translateTaskType(blueprint.taskType()), // 中文任务类型
                        blueprint.supplierNeedParams() // 任务类型的参数列表
                ))
                .collect(Collectors.toList());
    }
}