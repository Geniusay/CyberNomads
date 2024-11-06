package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BilibiliCommentActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliCommentReceiver;
import io.github.geniusay.core.supertask.TerminatorFactory;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.TerminatorConstants.TERMINATOR_TYPE_GROUP_COUNT;
import static io.github.geniusay.constants.TerminatorConstants.TERMINATOR_TYPE_TIMES;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.HOT_VIDEO_COMMENT;

@Component
public class BilibiliSureHotVideoCommentTaskBlueprint extends AbstractTaskBlueprint {

    @Resource
    GetHotVideoPlugin getHotVideoPlugin;

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return HOT_VIDEO_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task) throws Exception {
        // 获取终结器
        Terminator terminator = task.getTerminator();

        // 获取任务参数
        Map<String, Object> params = task.getParams();
        String commentStr = (String) params.get("commentStr");
        String cookie = robot.getCookie();
        int videoCount = (int) params.get("videoCount");  // 获取用户指定的评论视频数量

        // 获取指定数量的热门视频
        List<VideoDetail> videoList = getHotVideoPlugin.getHandleVideoWithLimit(params, videoCount);

        // 从 RobotWorker 创建 BilibiliUserActor
        BilibiliUserActor actor = new BilibiliUserActor(robot.getId().toString(), robot.getNickname(), cookie);

        // 创建评论行为逻辑
        BilibiliCommentActionLogic commentAction = new BilibiliCommentActionLogic(commentStr);

        // 遍历视频列表，逐个发送评论（实现一对一映射）
        for (VideoDetail video : videoList) {
            BilibiliCommentReceiver receiver = new BilibiliCommentReceiver(String.valueOf(video.getData().getAid()));
            new ActionFlow<>(actor, commentAction, receiver).execute();
        }
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        Terminator terminator = task.getTerminator();
        String robotName = robot.getNickname();
        String commentStr = (String) task.getParams().get("commentStr");

        if (terminator.taskIsDone()) {
            return String.format("[Success] %s robot 已经对所有指定视频发表评论，评论内容是: %s", robotName, commentStr);
        } else {
            return String.format("[Error] %s robot 执行任务失败，未能对所有视频发表评论", robotName);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TerminatorFactory.getTerminatorParams(TERMINATOR_TYPE_GROUP_COUNT, TERMINATOR_TYPE_TIMES),
                new TaskNeedParams("hotVideoSearch", "热门视频指定参数", true, getHotVideoPlugin.supplierNeedParams()),
                new TaskNeedParams("commentStr", String.class, "评论的内容", true, "赛博游民"),
                new TaskNeedParams("videoCount", Integer.class, "需要评论的视频数量", true, 1, null)
        );
    }
}
