package io.github.geniusay.async;

import io.github.geniusay.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AsyncService {

    @Resource
    private EmailUtil emailUtil;

    @Async("taskExecutor")
    public void sendCodeToEmail(String to, String code) {
        log.info("向 email:{} 发送验证码:{}",to,code);
        emailUtil.sendCode(to, code);
    }
}
