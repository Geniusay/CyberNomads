package io.github.geniusay.service.Impl;

import io.github.common.web.Result;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.pojo.VO.PlatformVO;
import io.github.geniusay.service.CommonService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ICommonService implements CommonService {

    @Override
    public Result<?> getPlatforms() {
        return Result.success(Arrays.stream(Platform.values()).map(PlatformVO::platformConvertVO));
    }
}
