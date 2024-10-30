package io.github.geniusay.task;

import io.github.geniusay.task.enums.Platform;
import io.github.geniusay.task.enums.TaskType;
import io.github.geniusay.task.executor.BatchTaskExecutor;
import io.github.geniusay.task.executor.CompositeTaskExecutor;
import io.github.geniusay.task.executor.SimpleTaskExecutor;
import io.github.geniusay.task.executor.bilibili.BilibiliVideoTaskExecutor;
import io.github.geniusay.task.factory.BiliTaskFactory;
import io.github.geniusay.task.po.BatchTask;
import io.github.geniusay.task.po.CompositeTask;
import io.github.geniusay.task.po.SimpleTask;
import org.junit.Test;

public class TaskSystemDemo {

    @Test
    public void testBatchTask() {
        BiliTaskFactory biliTaskFactory = new BiliTaskFactory();

        // 创建一个简单任务执行器
        SimpleTaskExecutor simpleTaskExecutor = new SimpleTaskExecutor() {
            @Override
            public int executeSimpleTask(SimpleTask simpleTask) {
                // 模拟具体的简单任务执行逻辑
                System.out.println("Executing specific logic for simple task: " + simpleTask.getId());
                // 模拟任务执行失败的情况
                return Math.random() > 0.7 ? 0 : 1; // 70% 成功，30% 失败
            }
        };

        // 创建一个批量任务：100次点赞
        BatchTask batchLikeTask = biliTaskFactory.createBatchTask("founder123", TaskType.VIDEO_LIKE, 100, new BatchTaskExecutor(simpleTaskExecutor));
        System.out.println("Created BatchTask: 100 Likes");

        // 执行批量任务
        Boolean batchResult = batchLikeTask.execute();
        System.out.println("Batch Task execution result: " + batchResult);
    }

    @Test
    public void testCompositeTask() {
        BiliTaskFactory biliTaskFactory = new BiliTaskFactory();

        // 创建一个简单任务执行器
        SimpleTaskExecutor simpleTaskExecutor = new SimpleTaskExecutor() {
            @Override
            public int executeSimpleTask(SimpleTask simpleTask) {
                // 模拟具体的简单任务执行逻辑
                System.out.println("Executing specific logic for simple task: " + simpleTask.getId());
                return Math.random() > 0.7 ? 0 : 1; // 70% 成功，30% 失败
            }
        };

        // 创建一个复杂任务
        CompositeTask compositeTask = biliTaskFactory.createCompositeTask("founder123", new CompositeTaskExecutor());

        // 添加子任务
        compositeTask.addSubTask(biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_LIKE, simpleTaskExecutor));
        compositeTask.addSubTask(biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_FAV, simpleTaskExecutor));
        compositeTask.addSubTask(biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_COIN, simpleTaskExecutor));

        System.out.println("Created CompositeTask with 3 sub-tasks");

        // 执行复杂任务
        Boolean compositeResult = compositeTask.execute();
        System.out.println("Composite Task execution result: " + compositeResult);
    }

    @Test
    public void testPause() throws InterruptedException {
        BiliTaskFactory biliTaskFactory = new BiliTaskFactory();

        // 创建一个简单任务执行器
        SimpleTaskExecutor simpleTaskExecutor = new SimpleTaskExecutor() {
            @Override
            public int executeSimpleTask(SimpleTask simpleTask) {
                // 模拟具体的简单任务执行逻辑
                System.out.println("Executing specific logic for simple task: " + simpleTask.getId());
                return Math.random() > 0.7 ? 0 : 1; // 70% 成功，30% 失败
            }
        };

        // 创建批量任务
        BatchTask batchTask = biliTaskFactory.createBatchTask("founder123", TaskType.VIDEO_LIKE, 100, new BatchTaskExecutor(simpleTaskExecutor));
        BatchTaskExecutor batchTaskExecutor = (BatchTaskExecutor) batchTask.getExecutor();

        // 开始执行批量任务
        new Thread(() -> batchTaskExecutor.execute(batchTask)).start();

        // 模拟暂停和恢复
        Thread.sleep(500); // 执行一段时间后暂停
        batchTaskExecutor.pause();
        System.out.println("Batch task paused.");

        Thread.sleep(2000); // 暂停2秒后恢复
        batchTaskExecutor.resume(batchTask);
        System.out.println("Batch task resumed.");

        // 创建复杂任务
        CompositeTask compositeTask = biliTaskFactory.createCompositeTask("founder123", new CompositeTaskExecutor());
        CompositeTaskExecutor compositeTaskExecutor = (CompositeTaskExecutor) compositeTask.getExecutor();

        // 添加子任务
        compositeTask.addSubTask(biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_LIKE, simpleTaskExecutor));
        compositeTask.addSubTask(biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_FAV, simpleTaskExecutor));
        compositeTask.addSubTask(biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_COIN, simpleTaskExecutor));

        // 开始执行复杂任务
        new Thread(() -> compositeTaskExecutor.execute(compositeTask)).start();

        // 模拟暂停和恢复
        Thread.sleep(500); // 执行一段时间后暂停
        compositeTaskExecutor.pause();
        System.out.println("Composite task paused.");

        Thread.sleep(2000); // 暂停2秒后恢复
        compositeTaskExecutor.resume(compositeTask);
        System.out.println("Composite task resumed.");
    }

    @Test
    public void testVideoExecutor() {
        // 创建B站视频任务执行器
        BilibiliVideoTaskExecutor executor = new BilibiliVideoTaskExecutor();

        // 创建点赞任务
        SimpleTask likeTask = new SimpleTask("founder123", TaskType.VIDEO_LIKE, Platform.BILI, executor);
        likeTask.execute();

        // 创建收藏任务
        SimpleTask favTask = new SimpleTask("founder123", TaskType.VIDEO_FAV, Platform.BILI, executor);
        favTask.execute();

        // 创建投币任务
        SimpleTask coinTask = new SimpleTask("founder123", TaskType.VIDEO_COIN, Platform.BILI, executor);
        coinTask.execute();

        // 创建评论任务
        SimpleTask commentTask = new SimpleTask("founder123", TaskType.VIDEO_COMMENT, Platform.BILI, executor);
        commentTask.execute();
    }

    @Test
    public void testTaskPriority() {
        BiliTaskFactory biliTaskFactory = new BiliTaskFactory();

        // 创建一个简单任务执行器
        SimpleTaskExecutor simpleTaskExecutor = new SimpleTaskExecutor() {
            @Override
            public int executeSimpleTask(SimpleTask simpleTask) {
                System.out.println("Executing simple task with priority: " + simpleTask.getPriority() + " and ID: " + simpleTask.getId());
                return 1; // 模拟任务执行成功
            }
        };

        // 创建一个复杂任务
        CompositeTask compositeTask = biliTaskFactory.createCompositeTask("founder123", new CompositeTaskExecutor());

        // 添加不同优先级的子任务
        SimpleTask lowPriorityTask = biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_LIKE, simpleTaskExecutor);
        lowPriorityTask.setPriority(1); // 设置低优先级
        compositeTask.addSubTask(lowPriorityTask);

        SimpleTask mediumPriorityTask = biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_FAV, simpleTaskExecutor);
        mediumPriorityTask.setPriority(5); // 设置中等优先级
        compositeTask.addSubTask(mediumPriorityTask);

        SimpleTask highPriorityTask = biliTaskFactory.createSimpleTask("founder123", TaskType.VIDEO_COIN, simpleTaskExecutor);
        highPriorityTask.setPriority(10); // 设置高优先级
        compositeTask.addSubTask(highPriorityTask);

        System.out.println("Created CompositeTask with different priority sub-tasks");

        // 执行复杂任务
        Boolean compositeResult = compositeTask.execute();
        System.out.println("Composite Task execution result: " + compositeResult);
    }
}