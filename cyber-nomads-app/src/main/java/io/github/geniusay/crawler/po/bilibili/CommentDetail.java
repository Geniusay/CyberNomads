package io.github.geniusay.crawler.po.bilibili;

import lombok.Data;

@Data
public class CommentDetail {
    private String rpid;      // 根评论的rpid
    private String message;   // 评论内容
    private String date;      // 日期
    private long ctime;       // 评论时间（时间戳）
    private int like;         // 点赞数
    private int rcount;       // 回复数
    private String username;  // 评论者昵称
    private int level;        // 评论者的等级

    private String bvid;

    private String oid;
}