package io.github.geniusay.service.Impl;

import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DTO.WorkerTaskResponseDTO;
import io.github.geniusay.pojo.VO.TaskVO;
import io.github.geniusay.schedule.TaskScheduleManager;
import io.github.geniusay.service.TaskManagerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/24 21:29
 */
@Component
public class TaskManagerServiceImpl implements TaskManagerService {
    @Resource
    TaskScheduleManager manager;
    @Override
    public List<WorkerTaskResponseDTO> queryWorkerTasks(String workerId) {
        if(!StringUtils.isBlank(workerId)){
            Map<String, Task> taskMap = manager.getRobotTaskById(Long.valueOf(workerId));
            WorkerTaskResponseDTO dto = WorkerTaskResponseDTO.builder().workerId(workerId).taskId(new ArrayList<>(taskMap.keySet())).build();
            return List.of(dto);
        }else{
            List<WorkerTaskResponseDTO> res = new ArrayList<>();
            for (Long id : manager.getWorldRobotsTask().keySet()) {
                Map<String, Task> taskMap = manager.getRobotTaskById(id);
                res.add(WorkerTaskResponseDTO.builder().workerId(workerId).taskId(new ArrayList<>(taskMap.keySet())).build());
            }
            return res;
        }
    }

    @Override
    public List<TaskVO> queryTask(String taskId) {
        if(!StringUtils.isBlank(taskId)){
            return List.of(TaskVO.convert(manager.getTaskById(taskId)));
        }else{
            List<TaskVO> res = new ArrayList<>();
            for (String id : manager.getWorldTask().keySet()) {
                res.add(TaskVO.convert(manager.getTaskById(id)));
            }
            return res;
        }
    }
}
