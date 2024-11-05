package io.github.geniusay.pojo.VO;

import io.github.geniusay.pojo.Platform;
import io.github.geniusay.utils.TaskTranslationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformVO {
    private int code;

    private String platform;

    private String platformCnZh;

    public static PlatformVO platformConvertVO(Platform platform){
        return new PlatformVO(platform.getCode(), platform.getPlatform(), TaskTranslationUtil.translatePlatform(platform.getPlatform()));
    }
}
