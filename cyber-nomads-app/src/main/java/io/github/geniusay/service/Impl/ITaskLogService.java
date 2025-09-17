package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.mapper.TaskLogMapper;
import io.github.geniusay.pojo.DO.TaskLogDO;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.pojo.VO.TaskLogResponse;
import io.github.geniusay.pojo.VO.TaskLogVO;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.service.TaskLogService;
import io.github.geniusay.utils.LastWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ITaskLogService implements TaskLogService {

    private final TaskLogMapper taskLogMapper;

    public ITaskLogService(TaskLogMapper taskLogMapper) {
        this.taskLogMapper = taskLogMapper;
    }

    @Resource
    private TaskScheduleManager taskScheduleManager;

    /**
     * 根据任务的遗言记录日志到数据库
     */
    @Async
    @Override
    public void logTaskResult(RobotWorker robotWorker, String finalLastWord) {
        log.info("开始记录日志, robotName:{}, finalLstWord:{}", robotWorker.getNickname(), finalLastWord);
        Task task = robotWorker.getCurrentTask();
        boolean success = LastWordUtil.isSuccess(finalLastWord);
        // 构建 TaskLogDO 对象
        TaskLogDO taskLog = new TaskLogDO();
        taskLog.setSuccess(success);
        taskLog.setContent(finalLastWord);
        taskLog.setUid(task.getUid());
        taskLog.setTaskId(task.getId());
        taskLog.setTaskName(task.getTaskName());
        taskLog.setRobotId(robotWorker.getId().toString());
        taskLog.setRobotName(robotWorker.getNickname());

        task.getLoglist().add(taskLog);
        // 保存日志到数据库
        taskLogMapper.insert(taskLog);
//        robotWorker.setTask(null);
    }

    /**
     * 根据任务ID查询最近X条日志记录
     */
    @Override
    public TaskLogResponse getRecentLogsByTaskId(Long taskId, int limit) {
        if (limit > 20) {
            limit = 20;  // 限制最多返回20条日志
        }

        // 1. 从 TaskScheduleManager 中获取任务
        Task task = taskScheduleManager.getTaskById(String.valueOf(taskId));

        // 2. 如果任务存在并且 loglist 不为空，从内存中获取日志
        List<TaskLogVO> taskLogVOList = null;
        if (task != null && task.getLoglist() != null && !task.getLoglist().isEmpty()) {
            List<TaskLogDO> logList = task.getLoglist();

            taskLogVOList = logList.stream()
                    .sorted((log1, log2) -> log2.getCreateTime().compareTo(log1.getCreateTime()))  // 按创建时间倒序排列
                    .limit(limit)
                    .map(TaskLogVO::convertToTaskLogVO)
                    .collect(Collectors.toList());

            // 3. 返回 TaskLogResponse，包含任务状态和日志列表
            return new TaskLogResponse(task.getTaskStatus(), taskLogVOList);
        }

        // 4. 如果任务不存在或没有日志，从数据库中查询
        QueryWrapper<TaskLogDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId)
                .orderByDesc("create_time")  // 按创建时间倒序排列
                .last("LIMIT " + limit);     // 限制查询条数

        List<TaskLogDO> taskLogs = taskLogMapper.selectList(queryWrapper);

        taskLogVOList = taskLogs.stream()
                .map(TaskLogVO::convertToTaskLogVO)
                .collect(Collectors.toList());
        TaskStatus taskStatus = task != null ? task.getTaskStatus() : TaskStatus.PENDING;
        return new TaskLogResponse(taskStatus, taskLogVOList);
    }

    /**
     * 根据用户 UID 查询最近 20 条日志记录
     */
    @Override
    public List<TaskLogVO> getRecentLogsByUid(String uid) {
        QueryWrapper<TaskLogDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid)
                .orderByDesc("create_time")
                .last("LIMIT 20");

        List<TaskLogDO> taskLogs = taskLogMapper.selectList(queryWrapper);
        return taskLogs.stream()
                .map(TaskLogVO::convertToTaskLogVO)
                .collect(Collectors.toList());
    }
}
