package io.github.geniusay.blueprint;

import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.plugin.terminator.Terminator;
import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_COMMENT;

@Component
public class BilibiliVideoCommentTaskBlueprint extends AbstractTaskBlueprint {
    @Override
    public String platform() {
        return BILIBILI;
    }

    @Override
    public String taskType() {
        return VIDEO_COMMENT;
    }

    @Override
    protected void executeTask(RobotWorker robot, Task task, Terminator terminator) throws Exception {
        Map<String, Object> userParams = task.getParams();
        String oid = getValue(userParams,"oid",String.class);
        String text = getValue(userParams,"text",String.class);

        if(terminator.doTask(robot)){
            robot.setTask(task);
            BilibiliCommentApi.sendCommentOrReply(robot.getCookie(), oid, text, null, null);

        }
    }

    @Override
    protected String lastWord(RobotWorker robot, Task task) {
        return "";
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("bvid", String.class, "视频的BV号", false, ""),
                new TaskNeedParams("oid", String.class, "评论区id, 也就是视频的aid, 不传则通过BV去获取", false, ""),
                new TaskNeedParams("text",String.class,"评论内容")
        );
    }
}
