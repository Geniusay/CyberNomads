package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_COIN;

@Component
public class BilibiliVideoCoinTaskBlueprint extends AbstractTaskBlueprint {

    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return VIDEO_COIN;
    }

    @Override
    public TaskExecute supplierExecute() {
        return null;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("test", String.class, "测试一下视频投币", false, "")
        );
    }

    @Override
    public LastWordHandler supplierLastWordHandler() {
        return null;
    }
}
