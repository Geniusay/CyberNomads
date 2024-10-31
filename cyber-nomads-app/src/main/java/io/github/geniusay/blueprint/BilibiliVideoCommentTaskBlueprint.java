package io.github.geniusay.blueprint;

import io.github.geniusay.core.supertask.task.*;
import io.github.geniusay.core.supertask.taskblueprint.AbstractTaskBlueprint;
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

//    @Override
//    protected void executeTask(RobotWorker robot, Task task) throws Exception {
//        String cookie = robot.getCookie();
//        logProcessor.addLogToTask(task, "哥们已经拿到cookie了: " + cookie);
//
//        String oid = task.getParam("oid");
//        logProcessor.addLogToTask(task, "哥们又已经拿到oid了: " + oid);
//
//        String message = "hello welsir nt";
//        logProcessor.addLogToTask(task, "发送了一条消息: " + message);
//
////        BilibiliCommentApi.sendCommentOrReply(cookie, oid, message, null, null);
//        // 正常的完成结束
//        task.updateStatus(TaskStatus.COMPLETED, logProcessor, "给视频评论成功");
//    }

//    @Override
//    protected void executeTask(RobotWorker robot, Task task) throws Exception {
//        String cookie = robot.getCookie();
//        logProcessor.addLogToTask(task, "哥们已经拿到cookie了: " + cookie);
//
//        String oid = task.getParam("oid");
//        logProcessor.addLogToTask(task, "哥们又已经拿到oid了: " + oid);
//
//        String message = "hello welsir nt";
//        logProcessor.addLogToTask(task, "尝试发送一条消息: " + message);
//
//        try {
//            // 模拟调用 Bilibili API 进行评论，但失败
//            throw new RuntimeException("Bilibili API 调用失败: 无法评论");
//
//            // 正常情况下，API 调用成功
//            // BilibiliCommentApi.sendCommentOrReply(cookie, oid, message, null, null);
//            // task.updateStatus(TaskStatus.COMPLETED, logProcessor, "给视频评论成功");
//
//        } catch (Exception e) {
//            // 记录错误码和错误信息
//            task.addErrorCode("BILI_API_ERROR", e.getMessage());
//            logProcessor.addLogToTask(task, "[Error] 调用 Bilibili API 失败: " + e.getMessage());
//
//            // 设置任务状态为 FAILED
//            task.updateStatus(TaskStatus.FAILED, logProcessor, "[Error] 任务执行失败: " + e.getMessage());
//        }
//    }

    @Override
    public TaskExecute supplierExecute() {
        return null;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                new TaskNeedParams("bvid", "String", "视频的BV号", false, ""),
                new TaskNeedParams("oid", "String", "评论区id, 也就是视频的aid, 不传则通过BV去获取", false, ""),
                new TaskNeedParams("workid", "String", "指定您用来来做此任务的账号")
        );
    }

    @Override
    public LastWordHandler supplierLastWordHandler() {
        return null;
    }
}
