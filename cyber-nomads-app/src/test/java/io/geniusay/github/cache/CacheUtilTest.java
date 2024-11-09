package io.geniusay.github.cache;

import antlr.Token;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.mapper.PointRecordMapper;
import io.github.geniusay.mapper.UserMapper;
import io.github.geniusay.pojo.DO.PointRecord;
import io.github.geniusay.pojo.DTO.AddRobotDTO;
import io.github.geniusay.service.RobotService;
import io.github.geniusay.utils.CacheUtil;
import io.github.geniusay.utils.TokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
@WebAppConfiguration
public class CacheUtilTest {
    private static final Logger log = LoggerFactory.getLogger(CacheUtilTest.class);
    @Resource
    private CacheUtil cacheUtil;

    @Resource
    private RobotService robotService;

    @Resource
    private PointRecordMapper pointRecordMapper;

    @Test
    public void test() {
        robotService.addRobot(AddRobotDTO.builder()
                .username("xiaochun")
                .cookie("testCookie")
                .nickname("帅气的小春")
                .platform(1)
                .build());
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
    }
}
