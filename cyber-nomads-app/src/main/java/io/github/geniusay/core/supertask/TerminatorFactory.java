package io.github.geniusay.core.supertask;

import io.github.geniusay.constants.TerminatorConstants;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.plugin.terminator.TimesTerminator;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.utils.ConvertorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.TerminatorConstants.*;

@Slf4j
@Component
public class TerminatorFactory {

    /**
     * 根据任务的参数创建合适的任务终结器
     *
     * @param taskDO 任务对象
     * @return Terminator 实例
     */
    public Terminator createTerminator(TaskDO taskDO) {
        try {
            // 解析任务的 params 字段为 Map
            Map<String, Object> params = ConvertorUtil.jsonStringToMap(taskDO.getParams());

            // 获取 terminatorType 参数，决定使用哪种终结器
            String terminatorType = (String) params.get(TERMINATOR);

            switch (terminatorType) {
                case TERMINATOR_TYPE_GROUP_COUNT:
                    return new GroupCountTerminator(taskDO, params);
                case TERMINATOR_TYPE_TIMES:
                    return new TimesTerminator(taskDO, params);
                case COOL_DOWN_TYPE_TIMES:
                    return new CooldownTerminator(taskDO, params);
                default:
                    throw new RuntimeException("不支持的终结器类型: " + terminatorType);
            }
        } catch (RuntimeException e) {
            log.info("此任务未找到终结器参数:{}", taskDO.getId());
            return null;
        }
    }

    /**
     * 根据传入的终结器类型，返回对应的终结器参数
     */
    /**
     * 根据传入的终结器类型，返回对应的终结器参数
     */
    public static TaskNeedParams getTerminatorParams(String... terminatorTypes) {
        List<TaskNeedParams> selection = new ArrayList<>();

        // 遍历传入的终结器类型，获取相应的参数
        for (String terminatorType : terminatorTypes) {
            switch (terminatorType) {
                case TERMINATOR_TYPE_GROUP_COUNT:
                    selection.add(GroupCountTerminator.getTerminatorParams());
                    break;
                case TERMINATOR_TYPE_TIMES:
                    selection.add(TimesTerminator.getTerminatorParams());
                    break;
                case COOL_DOWN_TYPE_TIMES:
                    selection.add(CooldownTerminator.getTerminatorParams());
                    break;
                default:
                    throw new IllegalArgumentException("不支持的终结器类型:  " + terminatorType);
            }
        }

        // 返回一个大的 TaskNeedParams，包含所有终结器的参数作为 selection
        return new TaskNeedParams(
                TerminatorConstants.TERMINATOR,
                String.class,
                "终结器参数",
                true,
                null,
                selection,
                null
        );
    }
}