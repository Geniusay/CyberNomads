package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;

import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;


import static io.github.geniusay.crawler.BCookie.cookie;

public class TestVideoAPI {

    @Test
    public void getVideoInfo() throws Exception {
        // 通过bvid获取视频详细信息
        ApiResponse<VideoDetail> response = BilibiliVideoApi.getVideoDetailById(cookie, "BV17q4y1z7ey");

        if (response.isSuccess()) {
            VideoDetail videoDetailByBvid = response.getData();
            System.out.println(videoDetailByBvid);

            // 输出视频标题和播放数
            if (videoDetailByBvid != null) {
                System.out.println("视频标题: " + videoDetailByBvid.getData().getTitle());
                System.out.println("播放数: " + videoDetailByBvid.getData().getStat().getView());
            } else {
                System.out.println("获取视频信息失败");
            }
        }
    }

    @Test
    public void likeVideo() throws Exception {
        String oid = "BV177D1YdEJa";
        BilibiliVideoApi.likeVideo(cookie, oid, 1);
    }

    @Test
    public void disLikeVideo() throws Exception {
        String bvid = "BV1TZ421E7Ci";
       BilibiliVideoApi.likeVideo(cookie, bvid, 2);
    }

    @Test
    public void coinVideo() throws Exception {
        String bvid = "BV1TZ421E7Ci";
        BilibiliVideoApi.coinVideo(cookie, bvid, 2, 1);
    }

    @Test
    public void favVideo() throws Exception {
        String aid = "1154116168";
        BilibiliVideoApi.favVideo(cookie, aid, "1273098564", null);
    }

    @Test
    public void tripleAction() throws Exception {
        String bvid = "BV1MLmjY9ES9";
        BilibiliVideoApi.tripleAction(cookie, bvid);
    }
}
