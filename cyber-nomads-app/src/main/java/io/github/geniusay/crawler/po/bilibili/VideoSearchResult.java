package io.github.geniusay.crawler.po.bilibili;

import lombok.Data;

import java.util.List;

/**
 * 描述: B站视频搜索结果数据模型
 */
@Data
public class VideoSearchResult {
    private int code;               // 状态码
    private String message;         // 消息
    private int ttl;                // 时间戳
    private Data data;              // 搜索结果数据

    @lombok.Data
    public static class Data {
        private String seid;        // 搜索ID
        private int page;           // 当前页码
        private int pagesize;       // 每页条数
        private int numResults;     // 总条数
        private int numPages;       // 总页数
        private List<Result> result; // 搜索结果列表

        @lombok.Data
        public static class Result {
            private String bvid;       // 视频bvid
            private long aid;          // 视频aid
            private String title;      // 视频标题
//            private String description; // 视频描述
//            private String pic;        // 视频封面图URL
            private String author;     // 作者名称
            private long mid;          // 作者ID
//            private int play;          // 播放量
//            private int favorites;     // 收藏量
//            private int like;          // 点赞数
//            private int danmaku;       // 弹幕数
//            private String duration;   // 视频时长
            private String arcurl;     // 视频链接
            private long senddate;     // 发布时间戳
//            private String tag;        // 标签
        }
    }
}