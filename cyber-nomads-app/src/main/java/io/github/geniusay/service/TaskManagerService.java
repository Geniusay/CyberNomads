package io.github.geniusay.service;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.pojo.DTO.WorkerTaskResponseDTO;
import io.github.geniusay.pojo.VO.TaskVO;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/24 21:26
 */
public interface TaskManagerService {

    List<WorkerTaskResponseDTO> queryWorkerTasks(String workerId);

    List<TaskVO> queryTask(String taskId);

}
