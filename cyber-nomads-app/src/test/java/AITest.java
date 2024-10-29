import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.utils.AIGenerate.AIGenerateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

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

}
