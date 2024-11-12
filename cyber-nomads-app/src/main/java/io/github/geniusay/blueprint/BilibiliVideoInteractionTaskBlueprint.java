package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.selector.logic.AbstractLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliCoinLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliLikeLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliVideoReceiverSelector;
import io.github.geniusay.core.supertask.plugin.terminator.SingleUseTerminator;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.LOGIC_NAME;
import static io.github.geniusay.constants.PluginConstant.RECEIVER_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.LOGIC_SELECTOR_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.RECEIVER_SELECTOR_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_INTERACTION;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_LIKE;

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
                BiliVideoReceiverSelector.class
        ));
    }
}