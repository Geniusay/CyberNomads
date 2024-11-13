package io.github.geniusay.core.event.commonEvent;

import io.github.geniusay.core.event.Event;
import io.github.geniusay.core.supertask.task.RobotWorker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 18:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfterTaskExecuteEvent extends Event {

    private RobotWorker worker;
    private String taskId;
    private String lastTalk;
}
