package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BilibiliCommentActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliCommentReceiver;
import io.github.geniusay.core.supertask.TerminatorFactory;
import io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
        System.out.println(video.getData().getDesc());

        task.log("工作者 {} 对视频 {} 发表评论: {}", robot.getNickname(), video.getData().getAid(), comment);
        new ActionFlow<>(new BilibiliUserActor(robot), new BilibiliCommentActionLogic(comment), new BilibiliCommentReceiver(String.valueOf(video.getData().getAid()))).execute();
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        String robotName = robot.getNickname();
        return String.format("[Info] %s robot 正在不断对随机热门视频发表评论", robotName);
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TerminatorFactory.getTerminatorParams(COOL_DOWN_TYPE_TIMES),
                new TaskNeedParams("AiPrams", "ai相关参数", false, aiCommentGenerate.supplierNeedParams())
        );
    }
}