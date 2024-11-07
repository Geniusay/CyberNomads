//package io.github.geniusay.core.supertask;
//
//import io.github.geniusay.core.supertask.config.TaskConstant;
//import io.github.geniusay.core.supertask.task.RobotWorker;
//import io.github.geniusay.core.supertask.task.Task;
//import io.github.geniusay.core.supertask.config.TaskStatus;
//import io.github.geniusay.mapper.TaskLogMapper;
//import io.github.geniusay.pojo.DO.TaskLogDO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import static io.github.geniusay.core.supertask.config.TaskConstant.*;
//
//@SuppressWarnings("all")
//@Component
//public class TaskLogProcessor {
//
//    @Resource
//    TaskLogMapper logMapper;
//
//    private static final Logger logger = LoggerFactory.getLogger(TaskLogProcessor.class);
//
//    /**
//     * 在任务执行过程中，将日志加入到 logList 中
//     */
//    public void addLogToTask(Task task, String logContent) {
//        List<String> logList = (List<String>) task.getDataMap().get(LOG_LIST);
//        if (logList == null) {
//            logList = new CopyOnWriteArrayList<>();
//            task.getDataMap().put(LOG_LIST, logList);
//        }
//        logList.add(logContent);
//    }
//
//    /**
//     * 在任务执行完成后，将所有日志拼接成一个字符串，并异步保存到数据库
//     */
//    public void processFinalLog(RobotWorker robot, Task task) {
//
//        // 获取 logList 并拼接成一个完整的日志字符串
//        List<String> logList = (List<String>) task.getDataMap().get(LOG_LIST);
//        if (logList == null || logList.isEmpty()) {
//            return;
//        }
//
//        String finalLog = formatLogList(logList);
//
//        // 从任务中获取开始时间、结束时间、错误信息等
//        LocalDateTime startTime = (LocalDateTime) task.getDataMap().get(START_TIME);
//        LocalDateTime endTime = LocalDateTime.now();
//        String errorCode = task.getDataValOrDefault(TaskConstant.ERROR_CODE, String.class, null);
//        String errorMessage = task.getDataValOrDefault(TaskConstant.ERROR_MESSAGE, String.class, null);
//
//        // 判断任务是否成功
//        boolean success = task.getTaskStatus().equals(TaskStatus.COMPLETED);
//
//        TaskLogDO taskLogDO = new TaskLogDO(success, startTime, endTime, errorCode, errorMessage, finalLog);
//
//        // 异步保存到数据库
//        saveLogToDatabaseAsync(robot, taskLogDO);
//    }
//
//    /**
//     * 格式化日志列表，将每条日志带上序号
//     */
//    private String formatLogList(List<String> logList) {
//        StringBuilder finalLog = new StringBuilder();
//        for (int i = 0; i < logList.size(); i++) {
//            finalLog.append(i + 1).append(". ").append(logList.get(i)).append(";\n");
//        }
//        return finalLog.toString();
//    }
//
//    /**
//     * 异步保存日志到数据库
//     * @param robot RobotWorker
//     * @param logContent 拼接后的日志内容
//     */
//    @Async
//    public void saveLogToDatabaseAsync(RobotWorker robot, TaskLogDO taskLogDO) {
//        try {
//            // 插入日志到数据库
//            logMapper.insert(taskLogDO);
//            logger.info("日志成功保存到数据库: Robot [{}], Log: {}", robot.getNickname(), taskLogDO);
//        } catch (Exception e) {
//            logger.error("保存日志到数据库失败: Robot [{}], Error: {}", robot.getNickname(), e.getMessage(), e);
//        }
//    }
//}