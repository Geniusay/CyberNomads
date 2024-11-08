package io.geniusay.github.task;

import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.core.datasource.VideoDataPool;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
@WebAppConfiguration
public class GetHotVideoPluginTest {

    @Resource
    private VideoDataPool videoDataPool;

    @Test
    public void testGetPopularVideosWithCache() {
        List<BilibiliVideoDetail> rankingVideos = videoDataPool.getRankingVideos();
        List<BilibiliVideoDetail> popularVideos = videoDataPool.getPopularVideos();
    }
}
