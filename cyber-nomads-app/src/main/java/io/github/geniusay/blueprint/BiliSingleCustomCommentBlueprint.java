package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BiliCommentLogic;
import io.github.geniusay.core.actionflow.receiver.BiliCommentReceiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate;
import io.github.geniusay.core.supertask.plugin.comment.AbstractCommentGenerate;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
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

import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.SINGLE_VIDEO_CUSTOM_COMMENT;

@Slf4j
@Component
public class BiliSingleCustomCommentBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return SINGLE_VIDEO_CUSTOM_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        String comment = taskPluginFactory.<AbstractCommentGenerate>buildPluginWithGroup(COMMENT_GROUP_NAME, task).generateComment();
        BilibiliVideoDetail videoDetail = taskPluginFactory.<AbstractGetVideoPlugin>buildPluginWithGroup(GET_VIDEO_GROUP_NAME, task).getHandleVideo(robot, task);

        ApiResponse<Boolean> response = new ActionFlow<>(
                new BiliUserActor(robot),
                new BiliCommentLogic(comment),
                new BiliCommentReceiver(videoDetail)
        ).execute();
        task.addLastWord(robot, response, Map.of("bvid", videoDetail.getBvid(), "comment", comment));
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task, LastWord lastWord) {
        ApiResponse<Boolean> response = lastWord.getResponse();
        String bvid = (String) lastWord.getAdditionalInfo("bvid");
        String comment = (String) lastWord.getAdditionalInfo("comment");
        if (response.isSuccess()) {
            return LastWordUtil.buildLastWord(String.format("%s robot 成功对视频 %s 发表评论，评论内容: %s", robot.getNickname(), bvid, comment), true);
        } else {
            return LastWordUtil.buildLastWord(String.format("%s robot 发表评论失败，视频: %s，状态码: %d，错误消息: %s", robot.getNickname(), bvid, response.getCode(), response.getMsg()), false);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(CooldownTerminator.class, AICommentGenerate.class, GetHotVideoPlugin.class));
    }
}
