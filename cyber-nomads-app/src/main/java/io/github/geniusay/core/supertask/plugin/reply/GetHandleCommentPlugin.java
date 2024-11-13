package io.github.geniusay.core.supertask.plugin.reply;

import io.github.geniusay.core.supertask.plugin.TaskPlugin;
import io.github.geniusay.crawler.po.bilibili.CommentDetail;

public interface GetHandleCommentPlugin extends TaskPlugin {

    CommentDetail getHandleComment();
}
