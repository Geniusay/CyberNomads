package io.geniusay.github.task;

import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.core.supertask.plugin.video.GetHotVideoPlugin;
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
    private GetHotVideoPlugin getHotVideoPlugin;

    @Test
    public void testGetPopularVideosWithCache() {
        // 第一次调用，应该从API获取
        Map<String, Object> params = Map.of("videoType", "popular", "cacheDuration", 1L);
        List<VideoDetail> firstCall = getHotVideoPlugin.getHandleVideo(params);
        // 第二次调用，应该从缓存获取
        List<VideoDetail> secondCall = getHotVideoPlugin.getHandleVideo(params);
    }

    @Test
    public void testGetRankingVideosWithCache() {
        // 获取排行榜视频，设置缓存时间为2分钟
        Map<String, Object> params = Map.of("videoType", "ranking", "cacheDuration", 2L);
        List<VideoDetail> firstCall = getHotVideoPlugin.getHandleVideo(params);
        List<VideoDetail> secondCall = getHotVideoPlugin.getHandleVideo(params);
    }

    @Test
    public void testCacheExpiration() throws InterruptedException {
        // 设置缓存时间为1秒
        Map<String, Object> params = Map.of("videoType", "popular", "cacheDuration", 1L);
        List<VideoDetail> firstCall = getHotVideoPlugin.getHandleVideo(params);

        // 等待2秒，确保缓存失效
        Thread.sleep(2000);
        // 再次调用，应该重新从API获取
        List<VideoDetail> secondCall = getHotVideoPlugin.getHandleVideo(params);

    }
}
