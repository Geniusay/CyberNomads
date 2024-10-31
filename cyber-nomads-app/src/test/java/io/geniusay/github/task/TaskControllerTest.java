package io.geniusay.github.task;

import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.controller.TaskController;
import io.github.geniusay.utils.ThreadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
@WebAppConfiguration
public class TaskControllerTest {

    @Resource
    private TaskController taskController;

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

        taskController.createTask("视频评论任务22", "bilibili:", "video:comment:", params);
        taskController.createTask("视频评论任务33", "bilibili:", "video:comment:", params);
        ThreadUtil.set("uid", "test_user_123");

        taskController.createTask("视频评论任务44", "bilibili:", "video:comment:", params);
        taskController.createTask("视频评论任务55", "bilibili:", "video:comment:", params);
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

        taskController.createTask("视频评论任务", "bilibilqi:", "video:comment:", params);
    }
}