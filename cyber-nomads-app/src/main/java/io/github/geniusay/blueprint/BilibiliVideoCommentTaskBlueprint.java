package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.task.LogHandler;
import io.github.geniusay.core.supertask.task.TaskExecute;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
import io.github.geniusay.crawler.api.bilibili.BilibiliCommentApi;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public LogHandler supplierLog() {
        return (robot)->{
            String robotName = robot.getNickname();
            String comment = (String) robot.task().getDataVal(robotName + "result");
            robot.task().log("{} 给某视频评论了一条 {}", robotName, comment);
        };
    }

    @Override
    public TaskExecute supplierExecute() {
        return (robot)->{
            String cookie = robot.getCookie();
            String oid = robot.task().getParam("oid");
            String message = "hello welsir nt";
            BilibiliCommentApi.sendCommentOrReply(cookie, oid, message, null, null);
            robot.task().getDataMap().put(robot.getNickname() + "result",message);
            return null;
        };
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("bvid", "String", "视频的BV号", false, ""),
                new TaskNeedParams("oid", "String", "评论区id, 也就是视频的aid, 不传则通过BV去获取", false, ""),
                new TaskNeedParams("workid", "String", "指定您用来来做此任务的账号")
        );
    }
}