package io.github.geniusay.core.supertask.taskblueprint;

import io.github.geniusay.core.supertask.task.TaskExecute;
import io.github.geniusay.core.supertask.task.TaskNeedParams;

import java.util.List;


public interface TaskBlueprint {
    TaskExecute supplierExecute();
    List<TaskNeedParams> supplierNeedParams();
}
