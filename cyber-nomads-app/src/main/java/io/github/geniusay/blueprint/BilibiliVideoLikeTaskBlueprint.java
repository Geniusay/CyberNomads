package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BilibiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BilibiliLikeActionLogic;
import io.github.geniusay.core.actionflow.receiver.BilibiliVideoReceiver;
import io.github.geniusay.core.supertask.TerminatorFactory;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static io.github.geniusay.constants.TerminatorConstants.TERMINATOR_TYPE_GROUP_COUNT;
import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_LIKE;

@Slf4j
@Component
public class BilibiliVideoLikeTaskBlueprint extends AbstractTaskBlueprint {

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
        // 获取任务参数
        Map<String, Object> params = task.getParams();
        String videoId = (String) params.get("videoId");  // 单个视频的 bvid 或 aid
        log.info("拿到：{}", videoId);
        new ActionFlow<>(new BilibiliUserActor(robot), new BilibiliLikeActionLogic(), new BilibiliVideoReceiver(videoId)).execute();
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        Terminator terminator = task.getTerminator();
        String robotName = robot.getNickname();

        if (terminator.taskIsDone()) {
            return String.format("[Success] %s robot 已经完成对视频的点赞任务", robotName);
        } else {
            return String.format("[Error] %s robot 执行任务失败，未能完成所有点赞任务", robotName);
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TerminatorFactory.getTerminatorParams(TERMINATOR_TYPE_GROUP_COUNT),
                new TaskNeedParams("videoId", String.class, "需要点赞的单个视频ID (bvid 或 aid)", true, ""),
                new TaskNeedParams("likeCount", Integer.class, "需要点赞的次数, 这个可以不用填，在终结器参数里指定: groupCount", false, 1)
        );
    }
}