package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.pojo.DO.RobotDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskDispatcher {

    /**
     * 将任务派发给所有 RobotDO，返回对应的 RobotWorker 列表
     */
    public List<RobotWorker> dispatchToAll(Task task) {
        return task.getRobots().stream()
                .map(robotDO -> createRobotWorker(robotDO, task))
                .collect(Collectors.toList());
    }

    /**
     * 将任务派发给指定 ID 的 RobotDO，返回对应的 RobotWorker
     */
    public Optional<RobotWorker> dispatchToWorkerById(Task task, Long robotId) {
        return task.getRobots().stream()
                .filter(robotDO -> robotDO.getId().equals(robotId))
                .findFirst()
                .map(robotDO -> createRobotWorker(robotDO, task));
    }

    private RobotWorker createRobotWorker(RobotDO robotDO, Task task) {
        RobotWorker robotWorker = new RobotWorker(robotDO);
        robotWorker.setTask(task);
        return robotWorker;
    }
}