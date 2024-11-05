package io.github.geniusay.core.actionflow;

import io.github.geniusay.core.actionflow.actor.UserActor;
import io.github.geniusay.core.actionflow.frame.ActionFlow;
import io.github.geniusay.core.actionflow.frame.ActionMapping;
import io.github.geniusay.core.actionflow.frame.mapping.CustomMapping;
import io.github.geniusay.core.actionflow.frame.mapping.ManyToManyMapping;
import io.github.geniusay.core.actionflow.frame.mapping.ManyToOneMapping;
import io.github.geniusay.core.actionflow.frame.mapping.OneToManyMapping;
import io.github.geniusay.core.actionflow.logic.CommentActionLogic;
import io.github.geniusay.core.actionflow.logic.LikeActionLogic;
import io.github.geniusay.core.actionflow.receiver.VideoReceiver;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestActionFlow {

    @Test
    public void testOneToManyMapping() throws Exception {
        // 创建执行者
        UserActor actor = new UserActor("user123", "用户A");

        // 创建接收者列表
        List<VideoReceiver> videos = List.of(
                new VideoReceiver("video456", "视频B"),
                new VideoReceiver("video789", "视频C")
        );

        // 创建点赞行为逻辑
        LikeActionLogic likeAction = new LikeActionLogic();

        // 创建一对多映射
        ActionMapping<UserActor, VideoReceiver> mapping = new OneToManyMapping<>();

        // 显式声明泛型类型
        ActionFlow<UserActor, VideoReceiver> actionFlow = new ActionFlow<>(actor, likeAction, videos, mapping);

        // 执行行为流程
        actionFlow.execute();
    }

    @Test
    public void testManyToOneMapping() throws Exception {

        // 创建接收者列表
        List<UserActor> userActors = List.of(
                new UserActor("user123", "用户A"),
                new UserActor("user456", "用户B")
        );

        // 创建接收者
        VideoReceiver video = new VideoReceiver("video789", "视频C");

        // 创建评论行为逻辑
        CommentActionLogic commentAction = new CommentActionLogic("这是一个评论");

        // 创建多对一映射
        ActionMapping<UserActor, VideoReceiver> mapping = new ManyToOneMapping<>();

        // 创建行为流程并执行
        ActionFlow<UserActor, VideoReceiver> actionFlow = new ActionFlow<>(userActors, commentAction, video, mapping);
        actionFlow.execute();
    }

    @Test
    public void testManyToManyMapping() throws Exception {
        // 创建执行者列表
        UserActor actorA = new UserActor("user123", "用户A");
        UserActor actorB = new UserActor("user456", "用户B");

        // 创建接收者列表
        List<VideoReceiver> videos = List.of(
                new VideoReceiver("video456", "视频B"),
                new VideoReceiver("video789", "视频C")
        );

        // 创建点赞行为逻辑
        LikeActionLogic likeAction = new LikeActionLogic();

        // 创建多对多映射
        ActionMapping<UserActor, VideoReceiver> mapping = new ManyToManyMapping<>();

        // 创建行为流程并执行
        ActionFlow<UserActor, VideoReceiver> actionFlow = new ActionFlow<>(List.of(actorA, actorB), likeAction, videos, mapping);
        actionFlow.execute();
    }

    @Test
    public void testCustomMapping() throws Exception {
        // 创建执行者列表
        UserActor actorA = new UserActor("user123", "用户A");
        UserActor actorB = new UserActor("user456", "用户B");

        // 创建接收者列表
        VideoReceiver videoD = new VideoReceiver("video789", "视频D");
        VideoReceiver videoE = new VideoReceiver("video101", "视频E");

        // 自定义映射关系
        Map<UserActor, List<VideoReceiver>> customMapping = Map.of(
                actorA, List.of(videoD, videoE),
                actorB, List.of(videoD)
        );

        CommentActionLogic commentAction = new CommentActionLogic("自定义映射评论");
        ActionMapping<UserActor, VideoReceiver> mapping = new CustomMapping<>(customMapping);
        ActionFlow<UserActor, VideoReceiver> actionFlow = new ActionFlow<>(List.of(actorA, actorB), commentAction, List.of(videoD, videoE), mapping);
        actionFlow.execute();
    }
}