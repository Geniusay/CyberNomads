package io.github.geniusay.core.actionflow.frame;

/**
 * 描述: 接收者
 * @author suifeng
 * 日期: 2024/11/5
 */
public interface Receiver {
    String getId();    // 获取接收者的唯一标识
    String getType();  // 获取接收者的类型（如视频、用户、评论等）
}