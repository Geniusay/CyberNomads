package io.geniusay.github.task;

import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.controller.TaskController;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.service.TaskService;
import io.github.geniusay.utils.ConvertorUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
@WebAppConfiguration
public class TaskControllerTest {

    @Resource
    private TaskController taskController;

    @Autowired
    private TaskMapper taskMapper;

    @Resource
    private TaskService taskService;

    @Test
    public void testCreateTask() throws Exception {

        ThreadUtil.set("uid", "test_user_111");
        ThreadUtil.set("username", "testUser");

        // 构造请求参数
        Map<String, Object> params = Map.of(
            "bvid", "BV1xK4y1B7z9",
            "oid", "113384871690725",
            "workid", "robot123"
        );

//        taskController.createTask("视频评论任务22", "bilibili:", "video:comment:", params);
//        taskController.createTask("视频评论任务33", "bilibili:", "video:comment:", params);
//        ThreadUtil.set("uid", "test_user_123");
//
//        taskController.createTask("视频评论任务44", "bilibili:", "video:comment:", params);
//        taskController.createTask("视频评论任务55", "bilibili:", "video:comment:", params);
    }

    @Test
    public void testCreateTaskInvalid() throws Exception {

        ThreadUtil.set("uid", "test_user_111");
        ThreadUtil.set("username", "testUser");

        // 构造请求参数
        Map<String, Object> params = Map.of(
                "bvid", "BV1xK4y1B7z9",
                "oid", "113384871690725",
                "workid", "robot123"
        );

//        taskController.createTask("视频评论任务", "bilibilqi:", "video:comment:", params);
    }

    @Test
    public void testPopulateRobotListForTasks() {
//        // 1. 从数据库中获取所有任务
//        List<TaskDO> taskDOList = taskMapper.selectList(null);
//
//        List<TaskDO> populatedTaskList = taskService.populateRobotListForTasks(taskDOList, null);
//
//        for (TaskDO task : populatedTaskList) {
//            if (task.getRobots() != null && !task.getRobots().isEmpty()) {
//
//                List<Long> robotIds = ConvertorUtil.stringToList(task.getRobots());
//            }
//        }
    }
}