package io.github.geniusay.supertask.taskblueprint;

import io.github.geniusay.supertask.task.LogHandler;
import io.github.geniusay.supertask.task.TaskExecute;
import io.github.geniusay.supertask.task.TaskHelpParams;

import java.util.List;


public interface TaskBlueprint {

    LogHandler supplierLog();

    TaskExecute supplierExecute();

    List<TaskHelpParams> supplierHelpParams();
}
