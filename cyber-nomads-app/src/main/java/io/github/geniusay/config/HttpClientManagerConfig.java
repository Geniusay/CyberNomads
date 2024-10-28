package io.github.geniusay.config;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import io.github.geniusay.mapper.ProxyMapper;
import io.github.geniusay.pojo.Proxy;
import io.github.geniusay.proxy.ProxyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class HttpClientManagerConfig {
    @Resource
    private ProxyMapper proxyMapper;

    @Bean
    public ProxyManager proxyManager() {
        List<Proxy> proxies = proxyMapper.selectList(new LambdaQueryChainWrapper<>(proxyMapper));
        return new ProxyManager(proxies);
    }
}
