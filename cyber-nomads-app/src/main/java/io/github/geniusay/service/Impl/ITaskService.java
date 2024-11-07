package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.TaskParamValidator;
import io.github.geniusay.core.supertask.TaskStrategyManager;
import io.github.geniusay.core.supertask.config.TaskPlatformConstant;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.config.TaskTypeConstant;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.VO.TaskVO;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.geniusay.utils.TaskTranslationUtil.translatePlatform;
import static io.github.geniusay.utils.TaskTranslationUtil.translateTaskType;
import static io.github.geniusay.utils.ValidUtil.isValidConstant;

@Service
public class ITaskService implements TaskService {

    @Resource
    private TaskStrategyManager taskStrategyManager;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private RobotMapper robotMapper;

    @Resource
    private TaskStatusManager taskStatusManager;

    @Override
    @Transactional
    public TaskVO createTask(CreateTaskDTO create) {
        // 1. 获取用户信息
        String uid = ThreadUtil.getUid();
        String nickname = ThreadUtil.getNickname();

        // 2. 校验 platform 和 taskType
        validatePlatformAndTaskType(create.getPlatform(), create.getTaskType());

        // 3. 获取任务蓝图
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(create.getPlatform(), create.getTaskType());
        if (blueprint == null) {
            throw new ServeException("任务类型不支持: " + create.getTaskType());
        }

        // 4. 获取任务的必需参数并验证
        List<TaskNeedParams> needParams = blueprint.supplierNeedParams();
        TaskParamValidator.validateParams(needParams, create.getParams());

        // 5. 初始化 TaskDO 对象
        TaskDO taskDO = new TaskDO();
        taskDO.setUid(uid);
        taskDO.setNickname(nickname);
        taskDO.setTaskName(create.getTaskName());
        taskDO.setPlatform(create.getPlatform());
        taskDO.setTaskType(create.getTaskType());
        taskDO.setTaskStatus(TaskStatus.PENDING);
        taskDO.setParams(ConvertorUtil.mapToJsonString(create.getParams()));

        // 6. 校验并添加机器人账号
        if (create.getRobotIds() != null && !create.getRobotIds().isEmpty()) {
            // 校验机器人账号是否存在
            List<RobotDO> robotsFromDB = robotMapper.selectBatchIds(create.getRobotIds());
            if (robotsFromDB.size() != create.getRobotIds().size()) {
                throw new ServeException("部分机器人账号不存在");
            }

            // 校验机器人是否被禁用
            List<String> bannedRobots = robotsFromDB.stream()
                    .filter(RobotDO::isBan)  // 筛选出被禁用的机器人
                    .map(RobotDO::getNickname)  // 获取被禁用机器人的昵称
                    .collect(Collectors.toList());

            if (!bannedRobots.isEmpty()) {
                throw new ServeException("以下机器人账号已被禁用，无法添加: " + String.join(", ", bannedRobots));
            }

            // 将机器人 ID 转换为字符串并保存到任务中
            taskDO.setRobots(ConvertorUtil.listToString(create.getRobotIds()));
        } else {
            // 如果没有机器人账号，则设置为空
            taskDO.setRobots(ConvertorUtil.listToString(List.of()));
        }

        // 7. 保存任务到数据库
        taskMapper.insert(taskDO);
        // 8. 返回任务详情
        return TaskVO.convertToTaskVO(taskDO);
    }

    @Override
    @Transactional
    public TaskVO updateTask(UpdateTaskDTO updateTaskDTO) {
        String uid = ThreadUtil.getUid();

        // 1. 获取任务并校验权限
        TaskDO task = taskMapper.selectTaskByIdAndUid(updateTaskDTO.getTaskId(), uid);
        if (task == null) throw new ServeException("任务不存在或无权操作该任务: " + updateTaskDTO.getTaskId());

        // 2. 更新任务的基础字段
        updateTaskFields(task, updateTaskDTO);

        // 3. 更新机器人列表
        updateRobotsInTask(task, updateTaskDTO.getRobotIds());

        // 4. 更新任务参数
        updateTaskParams(task, updateTaskDTO.getParams());

        // 5. 统一保存更新后的 TaskDO 到数据库
        taskMapper.updateById(task);

        // 6. 返回更新后的任务详情
        return TaskVO.convertToTaskVO(task);
    }

    /**
     * 更新任务的基础字段（如任务名称、平台、任务类型等）
     */
    private void updateTaskFields(TaskDO task, UpdateTaskDTO updateTaskDTO) {
        if (updateTaskDTO.getTaskName() != null) {
            task.setTaskName(updateTaskDTO.getTaskName());
        }

        if (updateTaskDTO.getPlatform() != null) {
            task.setPlatform(updateTaskDTO.getPlatform());
        }

        if (updateTaskDTO.getTaskType() != null) {
            task.setTaskType(updateTaskDTO.getTaskType());
        }
    }

    /**
     * 更新机器人列表
     */
    private void updateRobotsInTask(TaskDO task, List<Long> robotIds) {
        if (robotIds == null || robotIds.isEmpty()) {
            return; // 如果没有传递机器人列表，直接返回
        }

        // 1. 校验机器人账号是否存在
        List<RobotDO> robotsFromDB = robotMapper.selectBatchIds(robotIds);
        if (robotsFromDB.size() != robotIds.size()) {
            throw new ServeException("部分机器人账号不存在");
        }

        // 2. 校验机器人是否被禁用
        List<String> bannedRobots = robotsFromDB.stream()
                .filter(RobotDO::isBan)  // 筛选出被禁用的机器人
                .map(RobotDO::getNickname)  // 获取被禁用机器人的昵称
                .collect(Collectors.toList());

        if (!bannedRobots.isEmpty()) {
            throw new ServeException("以下机器人账号已被禁用，无法添加: " + String.join(", ", bannedRobots));
        }

        // 3. 更新任务的机器人列表
        task.setRobots(ConvertorUtil.listToString(robotIds));
    }

    /**
     * 更新任务参数
     */
    private void updateTaskParams(TaskDO task, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return; // 如果没有传递参数，直接返回
        }

        // 获取任务的必需参数并验证
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(task.getPlatform(), task.getTaskType());
        List<TaskNeedParams> needParams = blueprint.supplierNeedParams();
        TaskParamValidator.validateParams(needParams, params);

        // 更新 params 字段
        task.setParams(ConvertorUtil.mapToJsonString(params));
    }

    @Override
    public List<TaskVO> getUserTasks() {
        QueryWrapper<TaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", ThreadUtil.getUid());
        List<TaskDO> taskDOList = taskMapper.selectList(queryWrapper);

        return taskDOList.stream()
                .map(TaskVO::convertToTaskVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, String>> getSupportedPlatforms() {
        return taskStrategyManager.getSupportedPlatforms().stream()
                .map(platform -> Map.of(
                        "key", platform, // 英文平台名称
                        "value", translatePlatform(platform) // 中文平台名称
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskFunctionDTO> getFunctionsAndParamsByPlatform(String platform) {
        return taskStrategyManager.getBlueprintsForPlatform(platform).stream()
                .map(blueprint -> new TaskFunctionDTO(
                        blueprint.taskType(), // 英文任务类型
                        translateTaskType(blueprint.taskType()), // 中文任务类型
                        blueprint.supplierNeedParams() // 任务类型的参数列表
                ))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void modifyTask(ModifyTaskDTO modifyTaskDTO) {
        String uid = ThreadUtil.getUid();

        // 1. 获取任务并校验权限
        TaskDO task = taskMapper.selectTaskByIdAndUid(modifyTaskDTO.getTaskId(), uid);
        if (task == null) throw new ServeException("任务不存在或无权操作该任务: " + modifyTaskDTO.getTaskId());

        // 2. 调用 TaskStatusManager 执行状态变更
        taskStatusManager.modifyTask(task, modifyTaskDTO.getAction());
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

    /**
     * 批量解析任务中的 robots 字段，并填充 robotList 字段
     */
    @Override
    public List<TaskDO> populateRobotListForTasks(List<TaskDO> taskDOList) {
        // 1. 创建一个 Set 来收集所有任务中的机器人 ID，避免重复查询
        Set<Long> allRobotIds = new HashSet<>();

        // 2. 遍历任务列表，解析每个任务中的 robots 字符串，收集机器人 ID
        for (TaskDO task : taskDOList) {
            List<Long> robotIds = ConvertorUtil.stringToList(task.getRobots());
            if (robotIds != null && !robotIds.isEmpty()) {
                allRobotIds.addAll(robotIds);
            }
        }

        // 3. 如果没有任何机器人 ID，直接返回原任务列表
        if (allRobotIds.isEmpty()) {
            return taskDOList;
        }

        // 4. 使用 robotMapper 批量查询所有的机器人信息
        List<RobotDO> allRobots = robotMapper.selectBatchIds(allRobotIds);

        // 5. 将查询到的机器人信息转换为 Map，方便后续根据 ID 进行查找
        Map<Long, RobotDO> robotMap = allRobots.stream()
                .collect(Collectors.toMap(RobotDO::getId, robot -> robot));

        // 6. 遍历任务列表，填充每个任务的 robotList 字段
        for (TaskDO task : taskDOList) {
            List<Long> robotIds = ConvertorUtil.stringToList(task.getRobots());
            if (robotIds != null && !robotIds.isEmpty()) {
                // 根据 robotIds 从 robotMap 中获取对应的机器人信息
                List<RobotDO> robotList = robotIds.stream()
                        .map(robotMap::get)
                        .filter(Objects::nonNull)  // 过滤掉可能为 null 的机器人
                        .collect(Collectors.toList());

                // 填充到任务的 robotList 字段
                task.setRobotList(robotList);
            }
        }

        // 7. 返回填充了 robotList 的任务列表
        return taskDOList;
    }

    @Override
    public List<TaskDO> getTaskByStatus(List<String> status) {
        return taskMapper.selectList(new QueryWrapper<TaskDO>().in("task_status", status));
    }
}
