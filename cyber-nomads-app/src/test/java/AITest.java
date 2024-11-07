import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.core.supertask.plugin.comment.CommentGenerate;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Map;
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
    @Resource
    CommentGenerate commentGenerate;
    @Test
    public void t(){
//        generateUtil.textGenerate("你好吗");
        String s = generateUtil.textGenerateAndReturnContent("请你以一个幽默有趣的身份来推销我们的产品名称，叫做cybernomads。他是一个用于全自动回复评论的软件，支持多任务，多账号等一键式自动化功能",300,"QQ号：xxx");
        System.out.println(s);
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
