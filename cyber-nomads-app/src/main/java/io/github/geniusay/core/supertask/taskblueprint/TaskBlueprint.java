package io.github.geniusay.core.supertask.taskblueprint;

import io.github.geniusay.core.supertask.task.LastWordHandler;
import io.github.geniusay.core.supertask.task.ParamsHelper;
import io.github.geniusay.core.supertask.task.TaskExecute;
import io.github.geniusay.core.supertask.task.ParamsHelper;



public interface TaskBlueprint extends ParamsHelper {
    TaskExecute supplierExecute();

    LastWordHandler supplierLastWordHandler();
}
