package io.github.geniusay.core.supertask.taskblueprint;

import io.github.geniusay.core.supertask.TaskLogProcessor;
import io.github.geniusay.core.supertask.config.TaskConstant;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskExecute;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static io.github.geniusay.core.supertask.config.TaskStatus.*;
import static io.github.geniusay.utils.TaskTranslationUtil.translatePlatform;
import static io.github.geniusay.utils.TaskTranslationUtil.translateTaskType;

public abstract class AbstractLogTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    public TaskLogProcessor logProcessor;

    @Override
    public TaskExecute supplierExecute() {
        return (robot) -> {
            Task task = robot.task();
            try {
                // 翻译平台和任务类型
                String platformInChinese = translatePlatform(platform());
                String taskTypeInChinese = translateTaskType(taskType());

                // 记录任务开始时间
                task.getDataMap().put(TaskConstant.START_TIME, LocalDateTime.now());

                // 逐行打印日志
                logProcessor.addLogToTask(task, "[Info] 开始执行任务");
                logProcessor.addLogToTask(task, "[Info] 平台: " + platformInChinese);
                logProcessor.addLogToTask(task, "[Info] 任务类型: " + taskTypeInChinese);
                logProcessor.addLogToTask(task, "[Info] 任务执行账号: " + robot.getId());
                logProcessor.addLogToTask(task, "[Info] 任务执行昵称: " + robot.getNickname());

                // 执行任务逻辑
                executeTask(robot, task);

                // 检查是否有 API 错误信息
                String errorCode = task.getDataValOrDefault(TaskConstant.ERROR_CODE, String.class, null);
                String errorMessage = task.getDataValOrDefault(TaskConstant.ERROR_MESSAGE, String.class, null);

                if (errorCode != null && errorMessage != null) {
                   // task.updateStatus(PARTIALLY_COMPLETED, logProcessor, "[Warning] 任务状态异常，API 错误: " + errorMessage);
                } else {
                    task.updateStatus(COMPLETED, logProcessor, "[Success] 任务执行成功");
                }

            } catch (Exception e) {
                // 设置任务状态为 FAILED，并记录错误日志
                //task.updateStatus(FAILED, logProcessor, "[Error] 任务执行失败: " + e.getMessage());
                task.addErrorCode("500", "程序内部错误: " + e.getMessage());
            } finally {
                // 任务完成后处理日志，拼接并保存到数据库
                logProcessor.processFinalLog(robot, task);
            }
            return null;
        };
    }

    /**
     * 子类实现具体的任务执行逻辑
     */
    protected abstract void executeTask(RobotWorker robot, Task task) throws Exception;
}
