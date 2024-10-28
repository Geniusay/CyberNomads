package io.github.geniusay.crawler.po.bilibili;

import lombok.Data;

import java.util.List;

@Data
public class CommentPage {
    private int pageNum;          // 当前页码
    private int pageSize;         // 每页项数
    private int totalCount;       // 总评论数
    private int totalPages;       // 总页数
    private List<CommentDetail> comments;  // 评论详情列表
}