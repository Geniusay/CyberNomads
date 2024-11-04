package io.github.geniusay.core.supertask;

import io.github.geniusay.constants.TerminatorConstants;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.plugin.terminator.TimesTerminator;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.utils.ConvertorUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TerminatorFactory {

    /**
     * 根据任务的参数创建合适的任务终结器
     *
     * @param taskDO 任务对象
     * @return Terminator 实例
     */
    public Terminator createTerminator(TaskDO taskDO) {
        // 解析任务的 params 字段为 Map
        Map<String, Object> params = ConvertorUtil.jsonStringToMap(taskDO.getParams());

        // 获取 terminatorType 参数，决定使用哪种终结器
        String terminatorType = (String) params.get("terminatorType");

        switch (terminatorType) {
            case TerminatorConstants.TERMINATOR_TYPE_GROUP_COUNT:
                return new GroupCountTerminator(taskDO, params);
            case TerminatorConstants.TERMINATOR_TYPE_TIMES:
                return new TimesTerminator(taskDO, params);
            default:
                throw new RuntimeException("不支持的终结器类型: " + terminatorType);
        }
    }

    public static TaskNeedParams getTerminatorParams() {
        List<TaskNeedParams> terminatorParamsList =
                List.of(new TaskNeedParams("terminatorType", String.class, "终结器类型"),
                        new TaskNeedParams("targetCount", Integer.class, "如果是总计次数终结器，传这个", false, 1),
                        new TaskNeedParams("singleTimes", Integer.class, "如果是单个工作者次数终结器，传这个", false, 1));
        return new TaskNeedParams("terminator", "终结器参数", true, terminatorParamsList);
    }
}