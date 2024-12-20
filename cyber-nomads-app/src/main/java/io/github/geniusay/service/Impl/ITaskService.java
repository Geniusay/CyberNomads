package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.constants.RCode;
import io.github.geniusay.core.cache.SharedRobotCache;
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
import io.github.geniusay.pojo.DO.SharedRobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.pojo.VO.TaskVO;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

    @Resource
    private SharedRobotCache sharedRobotCache;

    @Override
    @Transactional
    public TaskVO createTask(CreateTaskDTO create) {
        // 1. 获取用户信息
        String uid = ThreadUtil.getUid();
        String nickname = ThreadUtil.getNickname();

        // 2. 校验 platform 和 taskType
        validatePlatformAndTaskType(create);

        // 3. 获取任务蓝图
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(create.getPlatform(), create.getTaskType());
        if (blueprint == null) {
            throw new ServeException("任务类型不支持: " + create.getTaskType());
        }

        // 4，提纯params，同时获取任务的必需参数并验证
        create.setParams(blueprint.getParams(create.getParams()));

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
        validateAndAddRobots(taskDO, create.getRobotIds());

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

        // TODO 跟新任务状态判断
        if (task.inStatusList(TaskStatus.RUNNING, TaskStatus.EXCEPTION)) {
            throw new ServeException("任务当前状态不允许更新，只有未开始或暂停状态下的任务才可以更新");
        }

        // 2. 更新任务的基础字段
        updateTaskFields(task, updateTaskDTO);

        // 3. 校验并更新机器人列表
        validateAndAddRobots(task, updateTaskDTO.getRobotIds());

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
     * 校验并添加机器人账号到任务中
     */
    private void validateAndAddRobots(TaskDO taskDO, List<Long> robotIds) {
        if (robotIds == null || robotIds.isEmpty()) {
            // 如果没有传递机器人 ID，则设置为空列表
            taskDO.setRobots(ConvertorUtil.listToString(List.of()));
            return;
        }

        // 1. 校验机器人账号是否存在
        List<RobotDO> robotsFromDB = robotMapper.selectBatchIds(robotIds);
        if (robotsFromDB.size() != robotIds.size()) {
            throw new ServeException("部分机器人账号不存在");
        }

        // 2. 校验机器人是否属于当前用户
        String currentUserId = ThreadUtil.getUid();
        List<String> unauthorizedRobots = robotsFromDB.stream().filter(robot -> !robot.getUid().equals(currentUserId)).map(RobotDO::getNickname).collect(Collectors.toList());

        if (!unauthorizedRobots.isEmpty()) {
            throw new ServeException("以下机器人账号不属于当前用户，无法添加: " + String.join(", ", unauthorizedRobots));
        }

        // 3. 校验机器人是否被禁用
        List<String> bannedRobots = robotsFromDB.stream().filter(RobotDO::isBan).map(RobotDO::getNickname).collect(Collectors.toList());
        if (!bannedRobots.isEmpty()) {
            throw new ServeException("以下机器人账号已被禁用，无法添加: " + String.join(", ", bannedRobots));
        }

        // 4. 校验机器人是否属于当前任务的平台
        String taskPlatform = taskDO.getPlatform();
        List<String> invalidPlatformRobots = robotsFromDB.stream().filter(robot -> !PlatformUtil.getPlatformByCode(robot.getPlatform()).equals(taskPlatform))
                .map(RobotDO::getNickname).collect(Collectors.toList());

        if (!invalidPlatformRobots.isEmpty()) {
            throw new ServeException("以下机器人账号与任务平台不匹配，无法添加: " + String.join(", ", invalidPlatformRobots));
        }

        // 5. 将机器人 ID 转换为字符串并保存到任务中
        taskDO.setRobots(ConvertorUtil.listToString(robotIds));
    }

    /**
     * 更新任务参数
     */
    private void updateTaskParams(TaskDO task, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return; // 如果没有传递参数，直接返回
        }

        // 提纯params，并且获取任务的必需参数并验证
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(task.getPlatform(), task.getTaskType());
        params = blueprint.getParams(params);
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
     * TODO[FIX]:判断常量采用的是反射，固定常量可以为缓存在本地
     * 校验平台和任务类型是否合法
     */
    private void validatePlatformAndTaskType(CreateTaskDTO createTaskDTO) {
        String platform = createTaskDTO.getPlatform();
        String taskType = createTaskDTO.getTaskType();
        if (!isValidConstant(TaskPlatformConstant.class, platform)) {
            throw new ServeException("不支持的平台: " + platform);
        }

        if (!isValidConstant(TaskTypeConstant.class, taskType)) {
            throw new ServeException("不支持的任务类型: " + taskType);
        }

        if(createTaskDTO.getParams().size()>=30){
            throw new ServeException("过大的任务参数");
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

    /**
     * 删除任务
     */
    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        String uid = ThreadUtil.getUid();

        TaskDO task = taskMapper.selectTaskByIdAndUid(taskId,uid);
        if (task == null) {
            throw new ServeException("任务不存在或无权删除此任务: " + taskId);
        }

        if (task.inStatusList(TaskStatus.RUNNING, TaskStatus.EXCEPTION)) {
            throw new ServeException("任务当前状态不允许删除（进行中或异常状态下的任务不能删除）");
        }

        taskMapper.deleteById(taskId);
    }

    @Override
    public List<TaskDO> getTaskByStatus(List<String> status) {
        return taskMapper.selectList(new QueryWrapper<TaskDO>().in("task_status", status));
    }
    //TODO 实现检查共享robot对应信息
    private List<Long> checkSharedRobot(List<Long> ids, String taskType){
        // 检查robot是否废弃 使用pipeline去redis里面找可用的ids返回回来
        List<Long> list = sharedRobotCache.getSharedIds(ids);
        // 职业验证
        List<SharedRobotDO> sharedRobots = sharedRobotCache.getSharedRobotsById(list);
        for (SharedRobotDO sharedRobot : sharedRobots) {
            if (!sharedRobot.getFocusTask().contains(taskType)){
                throw new ServeException(RCode.ROBOT_TYPE_ERROR);
            }
        }
        return list;
    }
}
