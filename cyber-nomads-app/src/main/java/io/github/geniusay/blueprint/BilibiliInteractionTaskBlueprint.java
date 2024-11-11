package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.actionflow.logic.BiliFollowLogic;
import io.github.geniusay.core.actionflow.logic.BiliLikeLogic;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate;
import io.github.geniusay.core.supertask.plugin.comment.AbstractCommentGenerate;
import io.github.geniusay.core.supertask.plugin.selector.logic.AbstractLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliCoinLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliFollowLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.logic.BiliLikeLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.AbstractReceiverSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliUserReceiverSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliVideoReceiverSelector;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.SingleUseTerminator;
import io.github.geniusay.core.supertask.plugin.video.AbstractGetVideoPlugin;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
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
import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.INTERACTION;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_LIKE;

@Slf4j
@Component
public class BilibiliInteractionTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return INTERACTION;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {

        ActionLogic logic = taskPluginFactory.<AbstractLogicSelector>buildPluginWithGroup(COMMENT_GROUP_NAME, task).getLogic();
        Receiver receiver = taskPluginFactory.<AbstractReceiverSelector>buildPluginWithGroup(GET_VIDEO_GROUP_NAME, task).getReceiver();

        ApiResponse response = new ActionFlow<>(new BiliUserActor(robot), logic, receiver).execute();
        task.addLastWord(robot, response, Map.of(LOGIC_NAME, logic.getLogicName(), RECEIVER_NAME, receiver.getId()));
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        // 获取任务执行的最后结果
        LastWord lastWord = task.getLastWord(robot);
        if (lastWord == null) {
            return LastWordUtil.buildLastWord(robot.getNickname() + " robot 没有执行任务", false);
        }

        ApiResponse<Boolean> response = lastWord.getResponse();
        String logicName = (String) lastWord.getAdditionalInfo(LOGIC_NAME);
        String receiverName = (String) lastWord.getAdditionalInfo(RECEIVER_NAME);

        String actionDescription = String.format("%s robot 执行了 %s 行为，目标: %s",
                robot.getNickname(),
                logicName,
                receiverName);

        if (response.isSuccess()) {
            return LastWordUtil.buildLastWord(actionDescription + "，操作成功", true);
        } else {
            return LastWordUtil.buildLastWord(actionDescription + String.format("，操作失败，状态码: %d，错误消息: %s", response.getCode(), response.getMsg()), false);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(
                SingleUseTerminator.class,
                BiliCoinLogicSelector.class,
                BiliLikeLogicSelector.class,
                BiliFollowLogicSelector.class,
                BiliUserReceiverSelector.class,
                BiliVideoReceiverSelector.class
        ));
    }
}