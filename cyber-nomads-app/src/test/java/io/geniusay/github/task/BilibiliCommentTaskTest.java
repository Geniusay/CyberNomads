package io.geniusay.github.task;


import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.core.supertask.TaskDispatcher;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.core.supertask.TaskFactory;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.RobotWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.core.supertask.config.TaskPlatformConstant.BILIBILI;
import static io.github.geniusay.core.supertask.config.TaskTypeConstant.VIDEO_COMMENT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
@WebAppConfiguration
public class BilibiliCommentTaskTest {

    @Resource
    TaskFactory taskFactory;

    @Resource
    RobotMapper robotMapper;

    @Resource
    TaskDispatcher taskDispatcher;


    @Test
    public void comment(){
        TaskDO taskDO = new TaskDO();

        taskDO.setTaskName("shabi welsir");

        taskDO.setParams(Map.of("oid","113384871690725"));

        RobotDO robotDO = robotMapper.selectById("1851531734205771778");

        taskDO.setRobots(List.of(robotDO));

        Task task = taskFactory.buildTask(taskDO, BILIBILI, VIDEO_COMMENT);

        List<RobotWorker> robotWorkers = taskDispatcher.dispatchToAll(task);

        RobotWorker robotWorker = robotWorkers.get(0);

        robotWorker.task().getExecute().execute(robotWorker);

    }
}
