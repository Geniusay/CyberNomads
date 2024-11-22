package io.github.geniusay.crawler.po.bilibili;

import lombok.Data;

import java.util.List;

/**
 * 描述: B站热搜列表数据模型
 */
@Data
public class HotSearchResult {

    private int code; // 返回值，0 表示成功
    private String message; // 错误信息，默认为 success
    private long timestamp; // 榜单统计时间
    private List<HotWord> list; // 热搜词列表

    @Data
    public static class HotWord {
        private String keyword; // 热搜关键词
        private String show_name; // 完整关键词
        private int pos; // 名次
        private int word_type; // 条目属性
        private String icon; // 图标 URL
    }
}