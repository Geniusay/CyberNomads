package io.github.geniusay.core.supertask;

import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.core.supertask.task.RobotWorker;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RobotWorkerFactory {

    public List<RobotWorker> createRobotWorkers(List<RobotDO> robotDOList) {
        return robotDOList.stream()
                .map(RobotWorker::new)
                .collect(Collectors.toList());
    }
}