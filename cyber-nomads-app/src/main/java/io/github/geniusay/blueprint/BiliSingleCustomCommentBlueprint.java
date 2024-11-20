package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.comment.AbstractCommentGenerate;
import io.github.geniusay.core.supertask.plugin.comment.CustomCommentGenerate;
import io.github.geniusay.core.supertask.plugin.selector.logic.AbstractLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliCommentLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.AbstractReceiverSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliCommentReceiverSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliVideoReceiverSelector;
import io.github.geniusay.core.supertask.plugin.terminator.SingleUseTerminator;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.SINGLE_VIDEO_CUSTOM_COMMENT;

@Slf4j
@Component
public class BiliSingleCustomCommentBlueprint extends BiliAbstractInteractionBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    @Override
    public String taskType() {
        return SINGLE_VIDEO_CUSTOM_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        String comment = taskPluginFactory.<AbstractCommentGenerate>buildPluginWithGroup(COMMENT_GROUP_NAME, task).generateComment(robot);
        ActionLogic logic = taskPluginFactory.<AbstractLogicSelector>buildPluginWithGroup(LOGIC_SELECTOR_GROUP_NAME, task).getLogic(comment);
        Receiver receiver = taskPluginFactory.<AbstractReceiverSelector>buildPluginWithGroup(RECEIVER_SELECTOR_GROUP_NAME, task).getReceiver();
        ApiResponse<Boolean> response = new ActionFlow<>(new BiliUserActor(robot), logic, receiver).execute();
        task.addLastWord(robot, response, Map.of(LOGIC_NAME, logic.getLogicName(), RECEIVER_NAME, receiver.getId(), LOGIC_CONTENT, comment));
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(
                SingleUseTerminator.class,
                CustomCommentGenerate.class,
                BiliCommentLogicSelector.class,
                BiliVideoReceiverSelector.class
        ));
    }
}
