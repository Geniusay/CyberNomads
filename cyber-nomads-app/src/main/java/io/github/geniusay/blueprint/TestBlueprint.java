package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BilibiliCommentActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliCommentReceiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate;
import io.github.geniusay.core.supertask.plugin.comment.AbstractCommentGenerate;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.TEST_VIDEO_COMMENT;

@Slf4j
@Component
@Profile("dev")
public class TestBlueprint extends AbstractTaskBlueprint {

    @Resource
    GetHotVideoPlugin getHotVideoPlugin;

    @Resource
    TaskPluginFactory taskPluginFactory;

    private final boolean isTestMode = true;  // 设置测试模式

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return TEST_VIDEO_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        Map<String, Object> params = task.getParams();
        String comment = taskPluginFactory.<AbstractCommentGenerate>buildPluginWithGroup(COMMENT_GROUP_NAME, task).generateComment();
        VideoDetail video = getHotVideoPlugin.getHandleVideoWithLimit(params, 1).get(0);
        ApiResponse<Boolean> response = new ActionFlow<>(
                new BilibiliUserActor(robot),
                new BilibiliCommentActionLogic(comment, isTestMode),
                new BilibiliCommentReceiver(video.getData())
        ).execute();
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
            return LastWordUtil.buildLastWord(String.format("%s robot 成功对视频 %s 发表评论（测试模式），评论内容: %s", robot.getNickname(), bvid, comment), true);
        } else {
            return LastWordUtil.buildLastWord(String.format("%s robot 发表评论失败（测试模式），视频: %s，状态码: %d，错误消息: %s", robot.getNickname(), bvid, response.getCode(), response.getMsg()), false);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(CooldownTerminator.class, AICommentGenerate.class));
    }
}
