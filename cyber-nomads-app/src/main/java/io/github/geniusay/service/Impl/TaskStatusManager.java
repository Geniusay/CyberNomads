package io.github.geniusay.service.Impl;

import io.github.geniusay.core.supertask.config.TaskStatus;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.service.TaskStateChangeService;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

import static io.github.geniusay.constants.TaskActionConstant.*;

@Component
public class TaskStatusManager {

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TaskStateChangeService stateChangeService;

    /**
     * 根据 taskId 从数据库中查询任务，并修改任务状态
     */
    public void modifyTask(Long taskId, String action) {
        // 从数据库中查询 TaskDO
        TaskDO task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServeException("任务不存在: " + taskId);
        }

        // 调用现有的 modifyTask(TaskDO task, String action)
        modifyTask(task, action);
    }

    /**
     * 修改任务状态，支持删除、重置、开始、暂停、完成、失败、异常等操作
     */
    public void modifyTask(TaskDO task, String action) {
        TaskStatus currentStatus = task.getTaskStatus();

        switch (action.toLowerCase()) {
            case DELETE:
                deleteTask(task, currentStatus);
                break;
            case RESET:
                resetTask(task, currentStatus);
                break;
            case START:
                startTask(task, currentStatus);
                break;
            case PAUSE:
                pauseTask(task, currentStatus);
                break;
            case FINISH:
                finishTask(task, currentStatus);
                break;
            case FAILED:
                failTask(task, currentStatus);
                break;
            case EXCEPTION:
                exceptionTask(task, currentStatus);
                break;
            default:
                throw new ServeException("无效的操作类型: " + action);
        }
    }

    /**
     * 删除任务逻辑
     * 仅允许删除状态为 COMPLETED、FAILED 或 PENDING 的任务
     */
    private void deleteTask(TaskDO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.COMPLETED || taskStatus == TaskStatus.FAILED || taskStatus == TaskStatus.PENDING) {
            if (taskMapper.deleteById(task.getId()) > 0) {
                stateChangeService.notifyTaskDeleted(task, taskStatus);
            }
        } else {
            throw new ServeException("任务状态不允许删除: " + taskStatus);
        }
    }

    /**
     * 重置任务逻辑
     * 仅允许将状态为 PAUSED、FAILED 或 COMPLETED 的任务重置为 PENDING
     */
    private void resetTask(TaskDO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.PAUSED || taskStatus == TaskStatus.FAILED || taskStatus == TaskStatus.COMPLETED) {
            task.setTaskStatus(TaskStatus.PENDING);
            if (taskMapper.updateById(task) > 0) {
                stateChangeService.notifyTaskReset(task, taskStatus, TaskStatus.PENDING);
            }
        } else {
            throw new ServeException("任务状态不允许重置: " + taskStatus);
        }
    }

    /**
     * 开始任务逻辑
     * 仅允许将状态为 PENDING 或者 PAUSED 的任务修改为 RUNNING
     */
    private void startTask(TaskDO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.PENDING || taskStatus == TaskStatus.PAUSED) {
            task.setTaskStatus(TaskStatus.RUNNING);
            if (taskMapper.updateById(task) > 0) {
                stateChangeService.notifyTaskStarted(task, taskStatus, TaskStatus.RUNNING);
            }
        } else {
            throw new ServeException("任务状态不允许开始: " + taskStatus);
        }
    }

    /**
     * 暂停任务逻辑
     * 仅允许将状态为 RUNNING 或 EXCEPTION 的任务修改为 PAUSED
     */
    private void pauseTask(TaskDO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.RUNNING || taskStatus == TaskStatus.EXCEPTION) {
            task.setTaskStatus(TaskStatus.PAUSED);
            if (taskMapper.updateById(task) > 0) {
                stateChangeService.notifyTaskPaused(task, taskStatus, TaskStatus.PAUSED);
            }
        } else {
            throw new ServeException("任务状态不允许暂停: " + taskStatus);
        }
    }

    /**
     * 完成任务逻辑
     * 仅允许将状态为 RUNNING 或 EXCEPTION 的任务修改为 COMPLETED
     */
    private void finishTask(TaskDO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.RUNNING || taskStatus == TaskStatus.EXCEPTION) {
            task.setTaskStatus(TaskStatus.COMPLETED);
            if (taskMapper.updateById(task) > 0) {
                stateChangeService.notifyTaskFinished(task, taskStatus, TaskStatus.COMPLETED);
            }
        } else {
            throw new ServeException("任务状态流转错误: "+task.getTaskStatus()+"->" + taskStatus);
        }
    }

    /**
     * 任务错误逻辑
     * 仅允许将状态为 RUNNING 或 EXCEPTION 的任务修改为 FAILED
     */
    private void failTask(TaskDO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.RUNNING || taskStatus == TaskStatus.EXCEPTION) {
            task.setTaskStatus(TaskStatus.FAILED);
            if (taskMapper.updateById(task) > 0) {
                stateChangeService.notifyTaskFailed(task, taskStatus, TaskStatus.FAILED);
            }
        } else {
            throw new ServeException("任务状态不允许标记为失败: " + taskStatus);
        }
    }

    /**
     * 任务异常逻辑
     * 仅允许将状态为 RUNNING 的任务修改为 EXCEPTION
     */
    private void exceptionTask(TaskDO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.RUNNING) {
            task.setTaskStatus(TaskStatus.EXCEPTION);
            if (taskMapper.updateById(task) > 0) {
                stateChangeService.notifyTaskException(task, taskStatus, TaskStatus.EXCEPTION);
            }
        } else {
            throw new ServeException("任务状态不允许标记为异常: " + taskStatus);
        }
    }
}
