package io.github.geniusay.blueprint;


import io.github.geniusay.core.supertask.plugin.selector.logic.BiliCoinLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliLikeLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliTripletLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliVideoReceiverSelector;
import io.github.geniusay.core.supertask.plugin.terminator.SingleUseTerminator;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_INTERACTION;


@Slf4j
@Component
public class BilibiliVideoInteractionTaskBlueprint extends BilibiliAbstractInteractionTaskBlueprint {

    @Override
    public String taskType() {
        return VIDEO_INTERACTION;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(
                SingleUseTerminator.class,
                BiliLikeLogicSelector.class,
                BiliCoinLogicSelector.class,
                BiliTripletLogicSelector.class,
                BiliVideoReceiverSelector.class
        ));
    }
}