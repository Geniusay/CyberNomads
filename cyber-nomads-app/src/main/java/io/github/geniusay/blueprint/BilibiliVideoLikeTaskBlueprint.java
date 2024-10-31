package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.task.LogHandler;
import io.github.geniusay.core.supertask.task.TaskExecute;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
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
    public LogHandler supplierLog() {
        return (robot)->{
            // 这里可以实现日志逻辑
        };
    }

    @Override
    public TaskExecute supplierExecute() {
        return (robot)->{
            // 这里可以实现任务执行逻辑
            return null;
        };
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("test", "String", "测试一下视频点赞", false, "")
        );
    }
}