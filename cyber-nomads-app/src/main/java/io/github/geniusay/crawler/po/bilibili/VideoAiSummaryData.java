package io.github.geniusay.crawler.po.bilibili;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Data
public class VideoAiSummaryData {
    private int code;
    private ModelResult modelResult;
    private String stid;
    private int status;
    private int likeNum;
    private int dislikeNum;

    // 内部类：ModelResult
    @Data
    public static class ModelResult {
        private int resultType;
        private String summary;
        private List<Outline> outline;
    }

    // 内部类：Outline
    @Data
    public static class Outline {
        private String title;
        private List<PartOutline> partOutline;
        private int timestamp;
    }

    // 内部类：PartOutline
    @Data
    public static class PartOutline {
        private int timestamp;
        private String content;
    }

    /**
     * 工具方法：拼接 `summary` 和 `outline` 成一个完整的字符串
     *
     * @return 拼接后的文本
     */
    public  String generateFullSummary() {
        StringJoiner fullSummary = new StringJoiner("\n");

        // 1. 先拼接 summary
        if (modelResult != null && modelResult.getSummary() != null) {
            fullSummary.add("AI 总结: ").add(modelResult.getSummary()).add("\n");
        }

        // 2. 再拼接 outline 和 partOutline
        if (modelResult != null && modelResult.getOutline() != null) {
            fullSummary.add("详细提纲: ");
            List<Outline> outlines = modelResult.getOutline();
            for (Outline outline : outlines) {
                // 拼接每个 outline 的 title
                fullSummary.add("标题: " + outline.getTitle());

                // 拼接 partOutline 的内容
                if (outline.getPartOutline() != null) {
                    for (PartOutline part : outline.getPartOutline()) {
                        fullSummary.add("  - 内容: " + part.getContent() + " (时间戳: " + part.getTimestamp() + "秒)");
                    }
                }
            }
        }

        return fullSummary.toString();
    }

    /**
     * 自定义的 JSON 解析逻辑
     *
     * @param jsonString 原始 JSON 字符串
     * @return 解析后的 VideoAiSummaryData 对象
     */
    public static VideoAiSummaryData fromJson(String jsonString) {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // 创建 VideoAiSummaryData 对象
        VideoAiSummaryData summaryData = new VideoAiSummaryData();

        // 解析根对象的 code 字段
        summaryData.setCode(jsonObject.get("code").getAsInt());

        // 如果 code 不为 0，表示请求失败，抛出异常
        if (summaryData.getCode() != 0) {
            throw new IllegalArgumentException("请求失败，code: " + summaryData.getCode());
        }

        // 解析 data 对象
        JsonObject dataObject = jsonObject.getAsJsonObject("data");

        // 解析 stid, status, like_num, dislike_num
        summaryData.setStid(dataObject.has("stid") ? dataObject.get("stid").getAsString() : null);
        summaryData.setStatus(dataObject.has("status") ? dataObject.get("status").getAsInt() : 0);
        summaryData.setLikeNum(dataObject.has("like_num") ? dataObject.get("like_num").getAsInt() : 0);
        summaryData.setDislikeNum(dataObject.has("dislike_num") ? dataObject.get("dislike_num").getAsInt() : 0);

        // 解析 model_result 对象
        JsonObject modelResultObject = dataObject.getAsJsonObject("model_result");
        if (modelResultObject != null) {
            ModelResult modelResult = new ModelResult();
            modelResult.setResultType(modelResultObject.get("result_type").getAsInt());
            modelResult.setSummary(modelResultObject.get("summary").getAsString());

            // 解析 outline 数组
            JsonArray outlineArray = modelResultObject.getAsJsonArray("outline");
            if (outlineArray != null) {
                List<Outline> outlines = new ArrayList<>();
                for (int i = 0; i < outlineArray.size(); i++) {
                    JsonObject outlineObject = outlineArray.get(i).getAsJsonObject();
                    Outline outline = new Outline();
                    outline.setTitle(outlineObject.get("title").getAsString());
                    outline.setTimestamp(outlineObject.get("timestamp").getAsInt());

                    // 解析 part_outline 数组
                    JsonArray partOutlineArray = outlineObject.getAsJsonArray("part_outline");
                    if (partOutlineArray != null) {
                        List<PartOutline> partOutlines = new ArrayList<>();
                        for (int j = 0; j < partOutlineArray.size(); j++) {
                            JsonObject partOutlineObject = partOutlineArray.get(j).getAsJsonObject();
                            PartOutline partOutline = new PartOutline();
                            partOutline.setTimestamp(partOutlineObject.get("timestamp").getAsInt());
                            partOutline.setContent(partOutlineObject.get("content").getAsString());
                            partOutlines.add(partOutline);
                        }
                        outline.setPartOutline(partOutlines);
                    }
                    outlines.add(outline);
                }
                modelResult.setOutline(outlines);
            }
            summaryData.setModelResult(modelResult);
        }

        return summaryData;
    }
}