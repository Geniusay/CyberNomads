package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.mapper.TaskLogMapper;
import io.github.geniusay.pojo.DO.TaskLogDO;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.pojo.VO.TaskLogVO;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.service.TaskLogService;
import io.github.geniusay.utils.LastWordUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    public void logTaskResult(RobotWorker robotWorker) {
        Task task = robotWorker.getCurrentTask();
        String finalLastWord = robotWorker.task().getLastWord().lastTalk(robotWorker);
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
    }

    /**
     * 根据任务ID查询最近X条日志记录
     */
    @Override
    public List<TaskLogVO> getRecentLogsByTaskId(Long taskId, int limit) {
        if (limit > 20) {
            limit = 20;
        }

        // 1. 首先尝试从 TaskScheduleManager 中获取任务
        Task task = taskScheduleManager.getTaskById(String.valueOf(taskId));

        // 2. 如果任务存在并且 loglist 不为空，从内存中获取日志
        if (task != null && task.getLoglist() != null && !task.getLoglist().isEmpty()) {
            List<TaskLogDO> logList = task.getLoglist();

            return logList.stream()
                    .sorted((log1, log2) -> log2.getCreateTime().compareTo(log1.getCreateTime()))
                    .limit(limit)
                    .map(TaskLogVO::convertToTaskLogVO)
                    .collect(Collectors.toList());
        }

        QueryWrapper<TaskLogDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId)
                .orderByDesc("create_time")  // 按创建时间倒序排列
                .last("LIMIT " + limit);     // 限制查询条数

        List<TaskLogDO> taskLogs = taskLogMapper.selectList(queryWrapper);

        return taskLogs.stream()
                .map(TaskLogVO::convertToTaskLogVO)
                .collect(Collectors.toList());
    }
}
