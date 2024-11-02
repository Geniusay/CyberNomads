package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.TaskParamValidator;
import io.github.geniusay.core.supertask.TaskStrategyManager;
import io.github.geniusay.core.supertask.config.TaskPlatformConstant;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.config.TaskTypeConstant;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.RobotDO;
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

import static io.github.geniusay.constants.TaskActionConstant.DELETE;
import static io.github.geniusay.constants.TaskActionConstant.RESET;
import static io.github.geniusay.utils.AIGenerate.ValidUtil.isValidConstant;

@Service
public class ITaskService implements TaskService {

    @Resource
    private TaskStrategyManager taskStrategyManager;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private RobotMapper robotMapper;

    @Override
    @Transactional
    public TaskVO createTask(String taskName, String platform, String taskType, Map<String, Object> params) {
        // 1. 获取用户信息
        String uid = ThreadUtil.getUid();
        String nickname = ThreadUtil.getNickname();

        // 2. 校验 platform 和 taskType
        validatePlatformAndTaskType(platform, taskType);

        // 3. 获取任务蓝图
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(platform, taskType);
        if (blueprint == null) {
            throw new ServeException("任务类型不支持: " + taskType);
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

        // 1. 获取任务
        TaskDO task = taskMapper.selectById(taskId);
        if (task == null) throw new ServeException("任务不存在: " + taskId);
        if (!uid.equals(task.getUid())) throw new ServeException("无权操作该任务");

        // 4. 获取任务的必需参数并验证
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(task.getPlatform(), task.getTaskType());
        List<TaskNeedParams> needParams = blueprint.supplierNeedParams();
        TaskParamValidator.validateParams(needParams, params);

        // 更新 params 字段
        task.setParams(ConvertorUtil.mapToJsonString(params));
        taskMapper.updateById(task);

        // 返回更新后的任务详情
        return convertToTaskVO(task);
    }



    @Override
    @Transactional
    public TaskVO updateRobotsInTask(Long taskId, List<Long> robotIds, boolean isAdd) {
        String uid = ThreadUtil.getUid();

        // 1. 获取任务
        TaskDO task = taskMapper.selectById(taskId);
        if (task == null) throw new ServeException("任务不存在: " + taskId);
        if (!uid.equals(task.getUid())) throw new ServeException("无权操作该任务");

        // 2. 校验机器人账号是否存在
        List<RobotDO> robotsFromDB = robotMapper.selectBatchIds(robotIds);
        if (robotsFromDB.size() != robotIds.size()) {
            throw new ServeException("部分机器人账号不存在");
        }

        // 3. 获取当前任务的机器人列表
        List<Long> currentRobotIds = ConvertorUtil.stringToList(task.getRobots());
        if (currentRobotIds == null) {
            currentRobotIds = new ArrayList<>();
        }

        // 4. 添加或删除机器人
        if (isAdd) {
            // 添加机器人，避免重复添加
            for (Long robotId : robotIds) {
                if (!currentRobotIds.contains(robotId)) {
                    currentRobotIds.add(robotId);
                }
            }
        } else {
            // 删除机器人
            currentRobotIds.removeAll(robotIds);
        }

        // 5. 更新任务的机器人列表
        task.setRobots(ConvertorUtil.listToString(currentRobotIds));
        taskMapper.updateById(task);

        // 6. 返回更新后的任务详情
        return convertToTaskVO(task);
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

    @Override
    @Transactional
    public void modifyTask(Long taskId, String action) {
        String uid = ThreadUtil.getUid();

        // 1. 获取任务
        TaskDO task = taskMapper.selectById(taskId);
        if (task == null) throw new ServeException("任务不存在: " + taskId);
        if (!uid.equals(task.getUid())) throw new ServeException("无权操作该任务");

        // 2. 根据操作类型执行不同的逻辑
        switch (action.toLowerCase()) {
            case DELETE:
                deleteTask(task);
                break;
            case RESET:
                resetTask(task);
                break;
            default:
                throw new ServeException("无效的操作类型: " + action);
        }
    }

    /**
     * 删除任务逻辑
     * 仅允许删除状态为 COMPLETED、FAILED 或 PENDING 的任务
     */
    private void deleteTask(TaskDO task) {
        if (task.getTaskStatus() == TaskStatus.COMPLETED || task.getTaskStatus() == TaskStatus.FAILED || task.getTaskStatus() == TaskStatus.PENDING) {
            taskMapper.deleteById(task.getId());
        } else {
            throw new ServeException("任务状态不允许删除: " + task.getTaskStatus());
        }
    }

    /**
     * 重置任务逻辑
     * 仅允许将状态为 PAUSED、FAILED 或 COMPLETED 的任务重置为 PENDING
     */
    private void resetTask(TaskDO task) {
        if (task.getTaskStatus() == TaskStatus.PAUSED || task.getTaskStatus() == TaskStatus.FAILED || task.getTaskStatus() == TaskStatus.COMPLETED) {
            task.setTaskStatus(TaskStatus.PENDING);
            taskMapper.updateById(task);
        } else {
            throw new ServeException("任务状态不允许重置: " + task.getTaskStatus());
        }
    }

    @Override
    @Transactional
    public void changeTaskStatus(Long taskId, String newStatus) {
        // 1. 获取任务
        TaskDO task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServeException("任务不存在: " + taskId);
        }

        // 2. 更新任务状态
        TaskStatus status;
        try {
            status = TaskStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ServeException("无效的任务状态: " + newStatus);
        }

        task.setTaskStatus(status);
        taskMapper.updateById(task);
    }

    /**
     * 校验平台和任务类型是否合法
     */
    private void validatePlatformAndTaskType(String platform, String taskType) {
        if (!isValidConstant(TaskPlatformConstant.class, platform)) {
            throw new ServeException("不支持的平台: " + platform);
        }

        if (!isValidConstant(TaskTypeConstant.class, taskType)) {
            throw new ServeException("不支持的任务类型: " + taskType);
        }
    }
}