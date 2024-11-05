package io.github.geniusay.core.actionflow.receiver;

import io.github.geniusay.core.actionflow.frame.Receiver;
import lombok.Data;

@Data
public class BilibiliCommentReceiver implements Receiver {

    private final String oid;  // 视频的oid（目标评论区ID）
    private final String rpid;  // 根评论的rpid
    private final String parentRpid;  // 父评论的rpid（可选）

    public BilibiliCommentReceiver(String oid, String rpid, String parentRpid) {
        this.oid = oid;
        this.rpid = rpid;
        this.parentRpid = parentRpid;
    }

    public BilibiliCommentReceiver(String oid) {
        this.oid = oid;
        this.rpid = null;
        this.parentRpid = null;
    }

    @Override
    public String getId() {
        return oid;
    }

    @Override
    public String getType() {
        return "评论";
    }
}