package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.stereotype.Component;  // 添加这个注解

import java.util.List;

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_LIKE;

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
    protected void executeTask(RobotWorker robot, Task task, Terminator terminator) throws Exception {

    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        return "";
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("test", String.class, "测试一下视频点赞", false, "")
        );
    }
}
