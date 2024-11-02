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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public TaskVO createTask(String taskName, String platform, String taskType, Map<String, Object> params) {
        // 1. 获取用户信息
        String uid = ThreadUtil.getUid();
        String nickname = ThreadUtil.getUsername();

        // 2. 校验 platform 和 taskType
        validatePlatformAndTaskType(platform, taskType);

        // 3. 获取任务蓝图
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(platform, taskType);
        if (blueprint == null) {
            throw new IllegalArgumentException("任务类型不支持: " + taskType);
        }

        // 4. 获取任务的必需参数并验证
        List<TaskNeedParams> needParams = blueprint.supplierNeedParams();
        TaskParamValidator.validateParams(needParams, params);

        // 5. 初始化 TaskDO 对象
        TaskDO taskDO = new TaskDO();
        taskDO.setUid(uid);
        taskDO.setNickname(nickname);
        taskDO.setTaskName(taskName);
        taskDO.setPlatform(platform);
        taskDO.setTaskType(taskType);
        taskDO.setTaskStatus(TaskStatus.PENDING);
        taskDO.setRobots(ConvertorUtil.listToString(List.of()));
        taskDO.setParams(ConvertorUtil.mapToJsonString(params));

        // 6. 保存任务到数据库
        taskMapper.insert(taskDO);

        // 7. 返回任务详情
        return convertToTaskVO(taskDO);
    }

    @Override
    @Transactional
    public TaskVO updateTaskParams(Long taskId, Map<String, Object> params) {
        String uid = ThreadUtil.getUid();

        // 获取任务
        TaskDO task = taskMapper.selectById(taskId);
        if (task == null) throw new IllegalArgumentException("任务不存在: " + taskId);
        if (!uid.equals(task.getUid())) {
            throw new RuntimeException("");
        }


        // 更新 params 字段
        task.setParams(ConvertorUtil.mapToJsonString(params));
        taskMapper.updateById(task);

        // 返回更新后的任务详情
        return convertToTaskVO(task);
    }

    @Override
    @Transactional
    public void updateRobotsInTask(Long taskId, List<Long> robotIds, boolean isAdd) {
        TaskDO task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在: " + taskId);
        }

        List<Long> robots = ConvertorUtil.stringToList(task.getRobots());
        if (robots == null) {
            robots = new ArrayList<>();
        }

        if (isAdd) {
            for (Long robotId : robotIds) {
                if (!robots.contains(robotId)) {
                    robots.add(robotId);
                }
            }
        } else {
            robots.removeAll(robotIds);
        }

        task.setRobots(ConvertorUtil.listToString(robots));
        taskMapper.updateById(task);
    }

    @Override
    public List<TaskVO> getUserTasks(String uid) {
        QueryWrapper<TaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        List<TaskDO> taskDOList = taskMapper.selectList(queryWrapper);

        return taskDOList.stream()
                .map(this::convertToTaskVO)
                .collect(Collectors.toList());
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

    /**
     * 将 TaskDO 转换为 TaskVO
     */
    private TaskVO convertToTaskVO(TaskDO taskDO) {
        Map<String, Object> params = ConvertorUtil.jsonStringToMap(taskDO.getParams());
        List<Long> robotIds = ConvertorUtil.stringToList(taskDO.getRobots());

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
    }

    private void validatePlatformAndTaskType(String platform, String taskType) {
        if (!isValidConstant(TaskPlatformConstant.class, platform)) {
            throw new IllegalArgumentException("不支持的平台: " + platform);
        }

        if (!isValidConstant(TaskTypeConstant.class, taskType)) {
            throw new IllegalArgumentException("不支持的任务类型: " + taskType);
        }
    }
}