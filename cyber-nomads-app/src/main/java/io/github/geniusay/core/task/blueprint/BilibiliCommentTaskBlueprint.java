package io.github.geniusay.core.task.blueprint;

import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.core.supertask.task.LogHandler;
import io.github.geniusay.core.supertask.task.TaskExecute;
import io.github.geniusay.core.supertask.task.TaskHelpParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_COMMENT;

@Component
public class BilibiliCommentTaskBlueprint extends AbstractTaskBlueprint {

    @Override
    public Platform platform() {
        return Platform.BILIBILI;
    }

    @Override
    public String taskType() {
        return VIDEO_COMMENT;
    }

    @Override
    public LogHandler supplierLog() {
        return (robot)->{
            String robotName = robot.robot().getNickname();
            String comment = (String) robot.task().getDataVal(robotName + "result");
            robot.task().log("{} 给某视频评论了一条 {}",robotName,comment);
        };
    }

    @Override
    public TaskExecute supplierExecute() {
        return (robot)->{
            String cookie = robot.robot().getCookie();
            String oid = robot.task().getParams("oid", String.class);
            String message = "hello welsir nt";
            BilibiliCommentApi.sendCommentOrReply(cookie, oid, message, null, null);
            robot.task().getDataMap().put(robot.robot().getNickname() + "result",message);
            return null;
        };
    }

    @Override
    public List<TaskHelpParams> supplierHelpParams() {
        return null;
    }
}
