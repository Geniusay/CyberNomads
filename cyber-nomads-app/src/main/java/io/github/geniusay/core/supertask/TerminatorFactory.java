package io.github.geniusay.core.supertask;

import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.pojo.DO.TaskDO;
import org.springframework.stereotype.Component;


@Component
public class TerminatorFactory {

    /**
     * 根据任务的参数创建合适的任务终结器
     */
    public Terminator createTerminator(TaskDO taskDO) {
        return null;
    }
}