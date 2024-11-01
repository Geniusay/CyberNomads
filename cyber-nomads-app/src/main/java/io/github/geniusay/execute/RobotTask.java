package io.github.geniusay.execute;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
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

    private String id;
    private RobotWorker worker;

    @Override
    public void run() {

    }

}
