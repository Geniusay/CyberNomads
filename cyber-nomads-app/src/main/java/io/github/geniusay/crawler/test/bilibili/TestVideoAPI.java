package io.github.geniusay.crawler.test.bilibili;

import io.github.geniusay.crawler.api.bilibili.BilibiliVideoApi;

import io.github.geniusay.crawler.po.bilibili.VideoAiSummaryData;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import org.junit.Test;


import java.util.List;

import static io.github.geniusay.crawler.BCookie.cookie;

public class TestVideoAPI {

    public static String imgKey = "7cd084941338484aae1ad9425b84077c";
    public static String subKey = "4932caff0ff746eab6f01bf08b70ac45";

    @Test
    public void getVideoInfo() throws Exception {
        // 通过bvid获取视频详细信息
        ApiResponse<VideoDetail> response = BilibiliVideoApi.getVideoDetailById(cookie, "BV1Rw4m1o7J5");

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
    public void getVideoAiSummary() throws Exception {
        String bvid = "BV14UUAYmExC";

        // 调用 API 获取 AI 总结
        ApiResponse<VideoAiSummaryData> response = BilibiliVideoApi.getVideoAiSummary(bvid, imgKey, subKey);

        // 检查请求是否成功
        if (response.isSuccess()) {
            VideoAiSummaryData data = response.getData();
            // 打印 modelResult 信息
            VideoAiSummaryData.ModelResult modelResult = data.getModelResult();
            if (modelResult != null) {
                System.out.println("Summary: " + modelResult.getSummary());

                // 打印 outline 信息
                List<VideoAiSummaryData.Outline> outlines = modelResult.getOutline();
                if (outlines != null && !outlines.isEmpty()) {
                    System.out.println("Outlines:");
                    for (int i = 0; i < outlines.size(); i++) {
                        VideoAiSummaryData.Outline outline = outlines.get(i);
                        System.out.println("  Outline " + (i + 1) + ":");
                        System.out.println("    Title: " + outline.getTitle());
                        System.out.println("    Timestamp: " + outline.getTimestamp());

                        // 打印 partOutline 信息
                        List<VideoAiSummaryData.PartOutline> partOutlines = outline.getPartOutline();
                        if (partOutlines != null && !partOutlines.isEmpty()) {
                            System.out.println("    Part Outlines:");
                            for (int j = 0; j < partOutlines.size(); j++) {
                                VideoAiSummaryData.PartOutline partOutline = partOutlines.get(j);
                                System.out.println("      Part " + (j + 1) + ":");
                                System.out.println("        Timestamp: " + partOutline.getTimestamp());
                                System.out.println("        Content: " + partOutline.getContent());
                            }
                        } else {
                            System.out.println("    No part outlines available.");
                        }
                    }
                }
            }

            System.out.println(data.generateFullSummary());
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
