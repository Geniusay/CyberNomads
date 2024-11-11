package io.github.geniusay.service.Impl;

import io.github.geniusay.common.Constant;
import io.github.geniusay.service.UserService;
import io.github.geniusay.util.HTTPUtils;
import org.springframework.stereotype.Service;

import static io.github.geniusay.common.Constant.*;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:16
 */
@Service
public class IUserServiceImpl implements UserService {
    @Override
    public String verifyTokenLegitimacy(String code) {
        HTTPUtils.postWithParams(HTTP + TARGET_PATH+POST_CODE_VERITY_URL,null,code);
        return null;
    }
}
