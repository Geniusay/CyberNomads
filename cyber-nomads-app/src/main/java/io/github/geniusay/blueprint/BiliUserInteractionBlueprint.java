package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.plugin.selector.logic.BiliFollowLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliUserReceiverSelector;
import io.github.geniusay.core.supertask.plugin.terminator.SingleUseTerminator;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.USER_INTERACTION;


@Slf4j
@Component
public class BiliUserInteractionBlueprint extends BiliAbstractInteractionBlueprint {

    @Override
    public String taskType() {
        return USER_INTERACTION;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(
                SingleUseTerminator.class,
                BiliFollowLogicSelector.class,
                BiliUserReceiverSelector.class
        ));
    }
}