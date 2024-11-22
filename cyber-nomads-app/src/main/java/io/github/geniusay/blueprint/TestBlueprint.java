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
import io.github.geniusay.core.supertask.plugin.video.KeywordSearchPlugin;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoAiSummaryData;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.core.supertask.config.PluginConstant.COMMENT_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.PluginConstant.GET_VIDEO_GROUP_NAME;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.TEST_VIDEO_COMMENT;

@Slf4j
@Component
@Profile("dev")
@Scope("prototype")
public class TestBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    private AbstractGetVideoPlugin getVideoPlugin;
    AbstractCommentGenerate commentGenerate;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return TEST_VIDEO_COMMENT;
    }

    @Override
    public void initBlueprint(Task task) {
        getVideoPlugin = taskPluginFactory.<AbstractGetVideoPlugin>buildPluginWithGroup(GET_VIDEO_GROUP_NAME, task);
        commentGenerate = taskPluginFactory.<AbstractCommentGenerate>buildPluginWithGroup(COMMENT_GROUP_NAME, task);
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        BilibiliVideoDetail videoDetail = getVideoPlugin.getHandleVideo(robot, task);
        String comment = commentGenerate.generateComment();
        ApiResponse<Boolean> response = new ActionFlow<>(new BiliUserActor(robot), new BiliCommentLogic(comment), new BiliCommentReceiver(videoDetail)).execute();
        task.addLastWord(robot, response, Map.of("bvid", videoDetail.getBvid(), "comment", comment));
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task, LastWord lastWord) {
        return "";
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(
                CooldownTerminator.class,
                AICommentGenerate.class,
                KeywordSearchPlugin.class
        ));
    }
}
