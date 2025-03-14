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
import io.github.geniusay.core.supertask.plugin.video.KeywordSearchPlugin;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoAiSummaryData;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.BASED_ON_CONTENT;
import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.INFINITY_HOT_VIDEO_COMMENT;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.INFINITY_HOT_VIDEO_SUMMARY;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.imgKey;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.subKey;

@Slf4j
@Component
@Scope("prototype")
public class BiliAiSummaryBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    private AbstractGetVideoPlugin getVideoPlugin;
    private AbstractCommentGenerate commentGenerate;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return INFINITY_HOT_VIDEO_SUMMARY;
    }

    @Override
    public void initBlueprint(Task task) {
        getVideoPlugin = taskPluginFactory.<AbstractGetVideoPlugin>buildPluginWithGroup(GET_VIDEO_GROUP_NAME, task);
        commentGenerate = taskPluginFactory.<AbstractCommentGenerate>buildPluginWithGroup(COMMENT_GROUP_NAME, task);
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        BilibiliVideoDetail videoDetail = getVideoPlugin.getHandleVideo(robot, task);
        ApiResponse<VideoAiSummaryData> videoAiSummary = BilibiliVideoApi.getVideoAiSummary(videoDetail.getBvid(), imgKey, subKey);

        String content = videoAiSummary.getData().generateFullSummary();
        String comment = commentGenerate.generateComment(content);
        ApiResponse<Boolean> response = new ActionFlow<>(new BiliUserActor(robot), new BiliCommentLogic(comment), new BiliCommentReceiver(videoDetail)).execute();
        task.addLastWord(robot, response, Map.of("bvid", videoDetail.getBvid(), "comment", comment));
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task, LastWord lastWord) {
        ApiResponse<Boolean> response = lastWord.getResponse();
        String bvid = (String) lastWord.getAdditionalInfo("bvid");
        String comment = (String) lastWord.getAdditionalInfo("comment");
        if (response.isSuccess()) {
            return LastWordUtil.buildLastWord(String.format("%s robot 成功对视频 %s 发表AI总结评论，评论内容: %s", robot.getNickname(), bvid, comment), true);
        } else {
            return LastWordUtil.buildLastWord(String.format("%s robot 发表AI总结评论失败，视频: %s，状态码: %d，错误消息: %s", robot.getNickname(), bvid, response.getCode(), response.getMsg()), false);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(
                CooldownTerminator.class,
                AICommentGenerate.class,
                GetHotVideoPlugin.class,
                KeywordSearchPlugin.class
        ));
    }
}
