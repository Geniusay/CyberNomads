package io.github.geniusay.utils;

import io.github.geniusay.core.supertask.task.TaskNeedParams;

import java.util.ArrayList;
import java.util.List;

public class ParamsUtil {
    @SafeVarargs
    public static List<TaskNeedParams> packageListParams(List<TaskNeedParams> ...taskNeedParams){
        ArrayList<TaskNeedParams> params = new ArrayList<>();
        for (List<TaskNeedParams> taskNeedParam : taskNeedParams) {
            params.addAll(taskNeedParam);
        }
        return params;
    }
}
