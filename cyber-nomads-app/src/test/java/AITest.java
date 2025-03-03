import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.core.ai.delegate.AIService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;

import static io.github.geniusay.constants.AIConstant.*;
import static io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate.CATGIRL_TEMPLATE;


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

    @Autowired
    StringEncryptor encryptor;

    @Test
    public void t(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", "Rockstar Games(R星）这家游戏公司的传奇故事。成立于\n" +
                "1998年，R星以其开创性的开放世界游戏如《侠盗猎车手》\n" +
                "系列、《荒野大镖客：救赎2》等，颠覆了游戏界。这些游\n" +
                "戏以其自由度、细节和剧情深度，赢得了玩家的广泛赞誉和\n" +
                "销量奇迹。R星以其对游戏品质的执着追求和对玩家体验的\n" +
                "深刻理解，成为了游戏行业的标杆和玩家心中的精神寄托。\n" +
                "要点·在进度条上展示\n" +
                "R星，游戏界传奇，开放世界神作，自由度极高。\n" +
                "00:01R星是一家颠覆游戏界的公司，擅长打造开放世\n" +
                "界的神作，代表作如《侠盗猎车手》。\n" +
                "00:38\n" +
                "《侠盗猎车手》打破了传统游戏的规则，提供了\n" +
                "前所未有的自由度，R星的名字因此刻入游戏\n" +
                "史。\n" +
                "01:41\n" +
                "《给他爱》在线模式让玩家互动，甚至能自己创\n" +
                "建任务和内容，成为史上最赚钱的娱乐产品之\n" +
                "—。\n" +
                "R星坚持初心，打造极致游戏体验。\n" +
                "02:02游戏以19世纪的美国西部为背景，极致真实感让\n" +
                "玩家震惊。\n" +
                "02:21第九艺术的极致，R星告诉全世界什么叫做极\n" +
                "致。\n" +
                "02:49R星从不随波逐流，始终坚持自己的初心，为玩\n" +
                "家带来最好的游戏体验。");
        map.put("combinedTemplate", CATGIRL_TEMPLATE);
        map.put("textCount", 100);
        map.put("slogan", "666");
        String generateEntity = aiService.execute(ROLE_COMMENT, DEEP_SEEK_R1, map);
        System.out.println(generateEntity);
    }

    @Test
    public void execute(){
        System.out.println(encryptor.encrypt("sk-qdahitijgevlgckknvypdmyvpveyhbrjqyylmhfkvuedmfsf"));
    }
}
