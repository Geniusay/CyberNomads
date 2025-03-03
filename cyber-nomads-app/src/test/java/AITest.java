import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.core.ai.delegate.AIService;
import io.github.geniusay.core.supertask.plugin.comment.CommentGenerate;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static io.github.geniusay.constants.AIConstant.QW_MODEL;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 1:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
@WebAppConfiguration
public class AITest {

    @Autowired
    AIService aiService;

    @Test
    public void t(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("prompt", "你好，请问你是什么？");
        Object generateEntity = aiService.execute("generate_entity", QW_MODEL, map);
        System.out.println(generateEntity);
    }

    @Test
    public void execute(){

    }
}
