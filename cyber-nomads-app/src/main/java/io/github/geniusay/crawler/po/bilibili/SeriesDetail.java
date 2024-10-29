package io.github.geniusay.crawler.po.bilibili;

import com.google.gson.JsonArray;
import lombok.Data;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

@Data
public class SeriesDetail {
    private SeriesConfig config; // 选期的配置信息
    private String reminder;     // 提醒信息
    private List<VideoDetail> list; // 选期的视频列表

    /**
     * 从JsonObject中创建SeriesDetail实例
     *
     * @param jsonObject JsonObject包含选期详细信息
     * @return SeriesDetail实例
     */
    public static SeriesDetail fromJson(JsonObject jsonObject) {
        SeriesDetail detail = new SeriesDetail();
        detail.setReminder(jsonObject.get("reminder").getAsString());

        // 解析config对象
        JsonObject configJson = jsonObject.getAsJsonObject("config");
        SeriesConfig config = SeriesConfig.fromJson(configJson);
        detail.setConfig(config);

        List<VideoDetail> videoDetailList = new ArrayList<>();

        JsonArray videoList = jsonObject.getAsJsonArray("list");
        if (videoList != null) {
            for (int i = 0; i < videoList.size(); i++) {
                JsonObject video = videoList.get(i).getAsJsonObject();
                VideoDetail videoDetail = VideoDetail.fromJson(video);
                // 将封装好的VideoDetail对象加入列表
                videoDetailList.add(videoDetail);
            }
        }

        detail.setList(videoDetailList);

        return detail;
    }
}

