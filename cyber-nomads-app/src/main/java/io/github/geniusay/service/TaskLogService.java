package io.github.geniusay.service;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.pojo.VO.TaskLogResponse;
import io.github.geniusay.pojo.VO.TaskLogVO;

import java.util.List;

/**
 * TaskLogService 接口，定义日志记录方法
 */
public interface TaskLogService {

    /**
     * 根据任务的遗言记录日志到数据库
     */
    void logTaskResult(RobotWorker robotWorker, String finalLastWord);

    /**
     * 根据任务ID查询最近X条日志记录
     */
    TaskLogResponse getRecentLogsByTaskId(Long taskId, int limit);

    /**
     * 根据用户 UID 查询最近 20 条日志记录
     */
    List<TaskLogVO> getRecentLogsByUid(String uid);
}
