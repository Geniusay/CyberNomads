package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BiliLikeLogic;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.SingleUseTerminator;
import io.github.geniusay.core.supertask.task.*;
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

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_LIKE;

@Slf4j
@Component
public class BilibiliVideoLikeTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    private static final String LINK_OR_ID = "linkOrId";

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return VIDEO_LIKE;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        String videoLinkOrId = task.getParam(LINK_OR_ID);
        ApiResponse<Boolean> response = new ActionFlow<>(
                new BiliUserActor(robot),
                new BiliLikeLogic(true),
                new BiliVideoReceiver(videoLinkOrId)
        ).execute();
        task.addLastWord(robot, response, Map.of("videoId", videoLinkOrId));
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        LastWord lastWord = task.getLastWord(robot);
        if (lastWord == null) {
            return LastWordUtil.buildLastWord(robot.getNickname() + " robot 没有执行任务", false);
        }

        ApiResponse<Boolean> response = lastWord.getResponse();
        String videoId = (String) lastWord.getAdditionalInfo("videoId");

        if (response.isSuccess()) {
            return LastWordUtil.buildLastWord(String.format("%s robot 成功对视频 %s 进行了点赞操作", robot.getNickname(), videoId), true);
        } else {
            return LastWordUtil.buildLastWord(String.format("%s robot 点赞失败，视频: %s，状态码: %d，错误消息: %s", robot.getNickname(), videoId, response.getCode(), response.getMsg()), false);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        List<TaskNeedParams> pluginParams = taskPluginFactory.pluginGroupParams(SingleUseTerminator.class);
        List<TaskNeedParams> blueprintParams = List.of(TaskNeedParams.ofKV(LINK_OR_ID, "", "需要点赞的视频链接或 BV 号"));
        return ParamsUtil.packageListParams(pluginParams, blueprintParams);
    }
}