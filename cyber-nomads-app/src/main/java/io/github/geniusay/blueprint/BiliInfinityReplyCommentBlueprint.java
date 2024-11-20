package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BiliCommentLogic;
import io.github.geniusay.core.actionflow.receiver.BiliCommentReceiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate;
import io.github.geniusay.core.supertask.plugin.comment.AbstractCommentGenerate;
import io.github.geniusay.core.supertask.plugin.reply.AbstractGetCommentPlugin;
import io.github.geniusay.core.supertask.plugin.reply.GetHotCommentPlugin;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;
import io.github.geniusay.crawler.po.bilibili.CommentDetail;
import io.github.geniusay.crawler.po.bilibili.VideoAiSummaryData;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;
import io.github.geniusay.utils.ParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.PluginConstant.BASED_ON_CONTENT;
import static io.github.geniusay.core.supertask.config.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.INFINITY_HOT_COMMENT_REPLY;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.imgKey;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.subKey;

@Slf4j
@Component
public class BiliInfinityReplyCommentBlueprint extends AbstractTaskBlueprint {

    @Resource
    TaskPluginFactory taskPluginFactory;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return INFINITY_HOT_COMMENT_REPLY;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        CommentDetail handleComment = taskPluginFactory.<AbstractGetCommentPlugin>buildPluginWithGroup(GET_COMMENT_GROUP_NAME, task).getHandleComment();
        String comment = taskPluginFactory.<AbstractCommentGenerate>buildPluginWithGroup(COMMENT_GROUP_NAME, task).generateComment(handleComment.getMessage());

        ApiResponse<Boolean> response = new ActionFlow<>(new BiliUserActor(robot), new BiliCommentLogic(comment), new BiliCommentReceiver(handleComment)).execute();
        task.addLastWord(robot, response, Map.of("bvid", handleComment.getBvid(), "username", handleComment.getUsername(), "comment", comment));
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task, LastWord lastWord) {
        ApiResponse<Boolean> response = lastWord.getResponse();
        String bvid = (String) lastWord.getAdditionalInfo("bvid");
        String username = (String) lastWord.getAdditionalInfo("username");
        String comment = (String) lastWord.getAdditionalInfo("comment");
        if (response.isSuccess()) {
            return LastWordUtil.buildLastWord(String.format("%s robot 成功在视频 %s 下回复用户 %s，回复内容: %s", robot.getNickname(), bvid, username, comment), true);
        } else {
            return LastWordUtil.buildLastWord(String.format("%s robot 在视频 %s 下回复用户 %s 失败，状态码: %d，错误消息: %s", robot.getNickname(), bvid, username, response.getCode(), response.getMsg()), false);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return ParamsUtil.packageListParams(taskPluginFactory.pluginGroupParams(
                CooldownTerminator.class,
                GetHotCommentPlugin.class,
                AICommentGenerate.class
        ));
    }
}
