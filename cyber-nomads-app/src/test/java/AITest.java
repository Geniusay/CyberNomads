import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 1:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
@WebAppConfiguration
public class AITest {

    @Resource
    AIGenerateUtil generateUtil;
    @Test
    public void t(){
        generateUtil.textGenerate("你好吗");
    }

    @Test
    public void execute(){
        // 封装请求
        // 读数据
        // 查视频
        // 生成对话内容
        /**
         *  【异步】调用bilibili评论功能接口，发评论 robotAsyncApi(WelsirRobot)
         *  1，把自己异步做的事情登记到 异步处理器, 需要通知任务中心
         */
        // 返回
        ExecutorService executorService = Executors.newCachedThreadPool();
    }


}
