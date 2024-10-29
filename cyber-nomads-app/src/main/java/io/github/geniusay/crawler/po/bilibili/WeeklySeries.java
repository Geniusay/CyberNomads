package io.github.geniusay.crawler.po.bilibili;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class WeeklySeries {
    private int number;    // 期数
    private String subject; // 主题
    private int status;     // 状态，2 表示已结束
    private String name;    // 名称，格式为 yyyy第n期 MM.dd - MM.dd

    /**
     * 根据 JsonObject 创建 WeeklySeries 实例
     * 
     * @param jsonObject JsonObject 包含期数信息
     * @return WeeklySeries 实例
     */
    public static WeeklySeries fromJson(JsonObject jsonObject) {
        WeeklySeries series = new WeeklySeries();
        series.setNumber(jsonObject.get("number").getAsInt());
        series.setSubject(jsonObject.get("subject").getAsString());
        series.setStatus(jsonObject.get("status").getAsInt());
        series.setName(jsonObject.get("name").getAsString());
        return series;
    }
}