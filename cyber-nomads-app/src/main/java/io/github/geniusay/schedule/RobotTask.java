package io.github.geniusay.schedule;

import io.github.geniusay.core.supertask.task.RobotWorker;
import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/31 20:15
 */
@Data
@Builder
public class RobotTask implements Runnable{
    private RobotWorker worker;

    @Override
    public void run() {
        worker.task().getExecute().execute(worker);
    }

}
