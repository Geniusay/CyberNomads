package io.github.geniusay.service.Impl;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.mapper.TaskLogMapper;
import io.github.geniusay.pojo.DO.TaskLogDO;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.service.TaskLogService;
import io.github.geniusay.utils.LastWordUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ITaskLogService implements TaskLogService {

    private final TaskLogMapper taskLogMapper;

    public ITaskLogService(TaskLogMapper taskLogMapper) {
        this.taskLogMapper = taskLogMapper;
    }

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
}