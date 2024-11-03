package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.DOUYING;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_COMMENT;

@Component
public class DouyingVideoCommentTaskBlueprint extends AbstractTaskBlueprint {

    @Override
    public String platform() {
        return DOUYING;
    }

    @Override
    public String taskType() {
        return VIDEO_COMMENT;
    }

    @Override
    public TaskExecute supplierExecute() {
        return null;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("bvid", String.class, "视频的BV号", false, ""),
                new TaskNeedParams("oid", String.class, "评论区id, 也就是视频的aid, 不传则通过BV去获取", false, ""),
                new TaskNeedParams("workid", String.class, "指定您用来来做此任务的账号")
        );
    }

    @Override
    public LastWordHandler supplierLastWordHandler() {
        return null;
    }
}
