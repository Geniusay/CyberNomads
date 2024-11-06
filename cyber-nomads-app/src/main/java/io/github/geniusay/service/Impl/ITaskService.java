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
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.VO.TaskVO;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.utils.ConvertorUtil;
import io.github.geniusay.utils.TaskTranslationUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
        return convertToTaskVO(taskDO);
    }

    @Override
    @Transactional
    public TaskVO updateTaskParams(UpdateTaskDTO update) {
        String uid = ThreadUtil.getUid();

        // 1. 获取任务并校验权限
        TaskDO task = taskMapper.selectTaskByIdAndUid(update.getTaskId(), uid);
        if (task == null) throw new ServeException("任务不存在或无权操作该任务: " + update.getTaskId());

        // 4. 获取任务的必需参数并验证
        AbstractTaskBlueprint blueprint = taskStrategyManager.getBlueprint(task.getPlatform(), task.getTaskType());
        List<TaskNeedParams> needParams = blueprint.supplierNeedParams();
        TaskParamValidator.validateParams(needParams, update.getParams());

        // 更新 params 字段
        task.setParams(ConvertorUtil.mapToJsonString(update.getParams()));
        taskMapper.updateById(task);

        // 返回更新后的任务详情
        return convertToTaskVO(task);
    }

    @Override
    @Transactional
    public TaskVO updateRobotsInTask(UpdateRobotsDTO updateRobotsDTO) {
        String uid = ThreadUtil.getUid();

        // 1. 获取任务并校验权限
        TaskDO task = taskMapper.selectTaskByIdAndUid(updateRobotsDTO.getTaskId(), uid);
        if (task == null) throw new ServeException("任务不存在或无权操作该任务: " + updateRobotsDTO.getTaskId());

        // 2. 校验机器人账号是否存在
        List<RobotDO> robotsFromDB = robotMapper.selectBatchIds(updateRobotsDTO.getRobotIds());
        if (robotsFromDB.size() != updateRobotsDTO.getRobotIds().size()) {
            throw new ServeException("部分机器人账号不存在");
        }

        // 3. 获取当前任务的机器人列表
        List<Long> currentRobotIds = ConvertorUtil.stringToList(task.getRobots());
        if (currentRobotIds == null) {
            currentRobotIds = new ArrayList<>();
        }
        Set<Long> robotIdSet = new HashSet<>(currentRobotIds);

        // 4. 添加或删除机器人
        if (updateRobotsDTO.isHasAdd()) {
            // 如果是添加操作，校验机器人是否被禁用
            List<String> bannedRobots = robotsFromDB.stream()
                    .filter(RobotDO::isBan)  // 筛选出被禁用的机器人
                    .map(RobotDO::getNickname)  // 获取被禁用机器人的昵称
                    .collect(Collectors.toList());

            if (!bannedRobots.isEmpty()) {
                throw new ServeException("以下机器人账号已被禁用，无法添加: " + String.join(", ", bannedRobots));
            }

            // 添加机器人，Set 自动去重
            robotIdSet.addAll(updateRobotsDTO.getRobotIds());
        } else {
            // 如果是删除操作，直接删除机器人，不做禁用状态的校验
            updateRobotsDTO.getRobotIds().forEach(robotIdSet::remove);
        }

        // 5. 将 Set 转换回 List，并更新任务的机器人列表
        task.setRobots(ConvertorUtil.listToString(new ArrayList<>(robotIdSet)));
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
