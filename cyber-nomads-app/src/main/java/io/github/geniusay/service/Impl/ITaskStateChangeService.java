package io.github.geniusay.service.Impl;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.service.TaskStateChangeService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ITaskStateChangeService implements TaskStateChangeService {

    @Resource
    private TaskScheduleManager manager;

    @Override
    public void notifyTaskDeleted(TaskDO task, TaskStatus oldStatus) {
        manager.removeWorkerTask(String.valueOf(task.getId()));
        log.info("任务已删除: {}，旧状态: {}", task.getTaskName(), oldStatus);
    }

    @Override
    public void notifyTaskReset(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        log.info("任务已重置: {}，从 {} 修改为 {}", task.getTaskName(), oldStatus, newStatus);
    }

    @Override
    public void notifyTaskStarted(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        // 判断任务是否有可用机器人
        if (StringUtil.isNullOrEmpty(task.getTaskName())) {
            throw new ServeException("请先添加可用机器人");
        }
        manager.registerTaskAndStart(task);
        log.info("任务已开始: {}，从 {} 修改为 {}", task.getTaskName(), oldStatus, newStatus);
    }

    @Override
    public void notifyTaskPaused(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        // 实现暂停任务的逻辑
        manager.removeWorkerTask(String.valueOf(task.getId()));
        log.info("任务已暂停: {}，从 {} 修改为 {}", task.getTaskName(), oldStatus, newStatus);
    }

    @Override
    public void notifyTaskFinished(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        // 实现完成任务的逻辑
        log.info("任务已完成: {}，从 {} 修改为 {}", task.getTaskName(), oldStatus, newStatus);
    }

    @Override
    public void notifyTaskRegister(TaskDO taskDO) {
        // 可以在这里记录任务注册的日志
        log.info("任务已注册: {}", taskDO.getTaskName());
    }

    @Override
    public void notifyTaskFailed(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        log.error("任务已失败: {}，从 {} 修改为 {}", task.getTaskName(), oldStatus, newStatus);
    }

    @Override
    public void notifyTaskException(TaskDO task, TaskStatus oldStatus, TaskStatus newStatus) {
        log.error("任务发生异常: {}，从 {} 修改为 {}", task.getTaskName(), oldStatus, newStatus);
    }
}