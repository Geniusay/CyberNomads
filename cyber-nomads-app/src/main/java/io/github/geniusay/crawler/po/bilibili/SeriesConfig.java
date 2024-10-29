package io.github.geniusay.crawler.po.bilibili;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class SeriesConfig {
    private int id;
    private String type;
    private int number;
    private String subject;
    private long stime;
    private long etime;
    private int status;
    private String name;
    private String label;
    private String hint;
    private int color;
    private String cover;
    private String shareTitle;
    private String shareSubtitle;
    private long mediaId;

    /**
     * 从JsonObject中创建SeriesConfig实例
     *
     * @param jsonObject JsonObject包含选期配置信息
     * @return SeriesConfig实例
     */
    public static SeriesConfig fromJson(JsonObject jsonObject) {
        SeriesConfig config = new SeriesConfig();
        config.setId(jsonObject.get("id").getAsInt());
        config.setType(jsonObject.get("type").getAsString());
        config.setNumber(jsonObject.get("number").getAsInt());
        config.setSubject(jsonObject.get("subject").getAsString());
        config.setStime(jsonObject.get("stime").getAsLong());
        config.setEtime(jsonObject.get("etime").getAsLong());
        config.setStatus(jsonObject.get("status").getAsInt());
        config.setName(jsonObject.get("name").getAsString());
        config.setLabel(jsonObject.get("label").getAsString());
        config.setHint(jsonObject.get("hint").getAsString());
        config.setColor(jsonObject.get("color").getAsInt());
        config.setCover(jsonObject.get("cover").getAsString());
        config.setShareTitle(jsonObject.get("share_title").getAsString());
        config.setShareSubtitle(jsonObject.get("share_subtitle").getAsString());
        config.setMediaId(jsonObject.get("media_id").getAsLong());

        return config;
    }
}