package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BilibiliCommentActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliCommentReceiver;
import io.github.geniusay.core.supertask.TerminatorFactory;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.TerminatorConstants.*;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.INFINITY_HOT_VIDEO_COMMENT;

@Slf4j
@Component
public class BilibiliHotVideoCommentTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    GetHotVideoPlugin getHotVideoPlugin;

    @Resource
    AICommentGenerate aiCommentGenerate;

    @Resource
    TaskPluginFactory taskPluginFactory;
    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return INFINITY_HOT_VIDEO_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        // 获取任务参数
        Map<String, Object> params = task.getParams();

        String comment = aiCommentGenerate.generateComment(params);
        VideoDetail video = getHotVideoPlugin.getHandleVideoWithLimit(params, 1).get(0);

        ApiResponse<Boolean> response = new ActionFlow<>(new BilibiliUserActor(robot), new BilibiliCommentActionLogic(comment), new BilibiliCommentReceiver(video.getData())).execute();

        Map<String, Object> additionalInfo = Map.of("bvid", video.getData().getBvid(), "comment", comment);
        LastWord lastWord = new LastWord(response, additionalInfo);
        task.addLastWord(robot, lastWord);
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        LastWord lastWord = task.getLastWord(robot);
        if (lastWord == null) {
            return LastWordUtil.buildLastWord(robot.getNickname() + " robot 没有执行任务", false);
        }

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
        return new ArrayList<>(taskPluginFactory.pluginGroupParams(CooldownTerminator.class, AICommentGenerate.class));
    }
}
