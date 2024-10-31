package io.github.geniusay.core.supertask.taskblueprint;

import io.github.geniusay.core.supertask.task.LogHandler;
import io.github.geniusay.core.supertask.task.TaskExecute;
import io.github.geniusay.core.supertask.task.TaskHelpParams;

import java.util.List;


public interface TaskBlueprint {

    LogHandler supplierLog();

    TaskExecute supplierExecute();

    List<TaskHelpParams> supplierHelpParams();
}
