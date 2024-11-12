package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.frame.ActionLogic;
import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.selector.logic.AbstractLogicSelector;
import io.github.geniusay.core.supertask.plugin.selector.receiver.BiliVideoReceiverSelector;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.LOGIC_NAME;
import static io.github.geniusay.constants.PluginConstant.RECEIVER_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.LOGIC_SELECTOR_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.RECEIVER_SELECTOR_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;

@Slf4j
public abstract class BilibiliAbstractInteractionTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        // 获取 Logic 和 Receiver（针对视频）
        ActionLogic logic = taskPluginFactory.<AbstractLogicSelector>buildPluginWithGroup(LOGIC_SELECTOR_GROUP_NAME, task).getLogic();
        Receiver receiver = taskPluginFactory.<BiliVideoReceiverSelector>buildPluginWithGroup(RECEIVER_SELECTOR_GROUP_NAME, task).getReceiver();

        // 执行任务
        ApiResponse<Boolean> response = new ActionFlow<>(new BiliUserActor(robot), logic, receiver).execute();
        // 保存任务结果
        task.addLastWord(robot, response, Map.of(LOGIC_NAME, logic.getLogicName(), RECEIVER_NAME, receiver.getId()));
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task, LastWord lastWord) {
        ApiResponse<Boolean> response = lastWord.getResponse();
        String logicName = (String) lastWord.getAdditionalInfo(LOGIC_NAME);
        String receiverName = (String) lastWord.getAdditionalInfo(RECEIVER_NAME);

        String actionDescription = String.format("%s robot 执行了 %s 行为，目标视频: %s",
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
       return null;
    }
}