package io.github.geniusay.blueprint;

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
    public TaskExecute supplierExecute() {
        return null;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("test", "String", "测试一下视频点赞", false, "")
        );
    }

    @Override
    public LastWordHandler supplierLastWordHandler() {
        return null;
    }
}
