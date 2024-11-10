package io.github.geniusay.core.actionflow.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoDetail;
import lombok.Data;

@Data
public class BilibiliCommentReceiver implements Receiver {

    private final String oid;  // 视频的oid（目标评论区ID）
    private final String rpid;  // 根评论的rpid
    private final String parentRpid;  // 父评论的rpid（可选）
    private final String bvid;

    public BilibiliCommentReceiver(String oid, String rpid, String parentRpid, String bvid) {
        this.oid = oid;
        this.rpid = rpid;
        this.parentRpid = parentRpid;
        this.bvid = bvid;
    }

    public BilibiliCommentReceiver(String oid) {
        this.oid = oid;
        this.rpid = null;
        this.parentRpid = null;
        this.bvid = null;
    }

    public BilibiliCommentReceiver(VideoDetail.Data data) {
        this.oid = String.valueOf(data.getAid());
        this.rpid = null;
        this.parentRpid = null;
        this.bvid = data.getBvid();
    }

    public BilibiliCommentReceiver(BilibiliVideoDetail videoDetail) {
        this.oid = String.valueOf(videoDetail.getAid());
        this.rpid = null;
        this.parentRpid = null;
        this.bvid = videoDetail.getBvid();
    }

    @Override
    public String getId() {
        return bvid;
    }

    @Override
    public String getType() {
        return "评论";
    }
}