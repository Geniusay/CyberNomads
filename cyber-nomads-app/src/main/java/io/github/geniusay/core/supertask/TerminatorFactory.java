package io.github.geniusay.core.supertask;

import io.github.geniusay.constants.TerminatorConstants;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.plugin.terminator.TimesTerminator;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.utils.ConvertorUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.TerminatorConstants.*;

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
        String terminatorType = (String) params.get(TERMINATOR);

        switch (terminatorType) {
            case TERMINATOR_TYPE_GROUP_COUNT:
                return new GroupCountTerminator(taskDO, params);
            case TERMINATOR_TYPE_TIMES:
                return new TimesTerminator(taskDO, params);
            default:
                throw new RuntimeException("不支持的终结器类型: " + terminatorType);
        }
    }

    /**
     * 根据传入的终结器类型，返回对应的终结器参数
     */
    public static TaskNeedParams getTerminatorParams(String... terminatorTypes) {
        List<TaskNeedParams> childParams = new ArrayList<>();

        // 遍历传入的终结器类型，获取相应的参数
        for (String terminatorType : terminatorTypes) {
            switch (terminatorType) {
                case TerminatorConstants.TERMINATOR_TYPE_GROUP_COUNT:
                    childParams.add(GroupCountTerminator.getTerminatorParams());
                    break;
                case TerminatorConstants.TERMINATOR_TYPE_TIMES:
                    childParams.add(TimesTerminator.getTerminatorParams());
                    break;
                default:
                    throw new IllegalArgumentException("不支持的终结器类型: " + terminatorType);
            }
        }

        // 返回一个大的 TaskNeedParams，包含所有终结器的参数作为 childParams
        return new TaskNeedParams(
                TerminatorConstants.TERMINATOR,
                "终结器参数",
                true,
                childParams
        );
    }
}