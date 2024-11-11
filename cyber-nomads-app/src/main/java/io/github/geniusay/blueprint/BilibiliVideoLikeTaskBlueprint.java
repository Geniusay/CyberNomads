package io.github.geniusay.blueprint;

import io.github.geniusay.core.actionflow.actor.BiliUserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.logic.BiliLikeLogic;
import io.github.geniusay.core.actionflow.receiver.BiliVideoReceiver;
import io.github.geniusay.core.supertask.plugin.TaskPluginFactory;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
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

    private static final String VIDEO_ID = "videoId";

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
        new ActionFlow<>(new BiliUserActor(robot), new BiliLikeLogic(), new BiliVideoReceiver(videoId)).execute();
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

        List<TaskNeedParams> pluginParams = taskPluginFactory.pluginGroupParams(GroupCountTerminator.class);

        List<TaskNeedParams> blueprintParams = List.of(TaskNeedParams.ofKV(VIDEO_ID, "", "需要点赞的单个视频ID (bvid 或 aid)"));

        return ParamsUtil.packageListParams(pluginParams, blueprintParams);
    }
}
