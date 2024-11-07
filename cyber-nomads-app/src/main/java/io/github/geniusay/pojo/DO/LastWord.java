package io.github.geniusay.pojo.DO;

import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class LastWord {

    private ApiResponse<Boolean> response;  // 执行结果
    private Map<String, Object> additionalInfo;  // 附加信息（如视频bvid、评论内容等）

    /**
     * 获取附加信息中的值
     * @param key 键
     * @return 值
     */
    public Object getAdditionalInfo(String key) {
        return additionalInfo.get(key);
    }
}