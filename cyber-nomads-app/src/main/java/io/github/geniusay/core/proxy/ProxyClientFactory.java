package io.github.geniusay.core.proxy;

import io.github.geniusay.core.proxy.retry.AbstractRetryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Valid
@Component
public class ProxyClientFactory {

    private final ConcurrentHashMap<String, ProxyClient> proxyClients = new ConcurrentHashMap<>();

    @Resource
    private ProxyPool proxyPool;

    /*
    * 1、创建client 参数：唯一标识key，重试时间，重试次数
    * */

    public ProxyClient createClient(@NotNull String key, @NotNull AbstractRetryStrategy retryStrategy){
        return proxyClients.computeIfAbsent(key, k -> {
            try {
                return new ProxyClient(retryStrategy, proxyPool.getProxy());
            } catch (Exception e) {
                log.error("代理获取异常");
                return new ProxyClient(retryStrategy);
            }
        });
    }

    public ProxyClient createClient(@NotNull String key, @NotNull AbstractRetryStrategy retryStrategy, Integer timeout){
        return proxyClients.computeIfAbsent(key, k -> {
            try {
                return new ProxyClient(retryStrategy, proxyPool.getProxy(), timeout);
            } catch (Exception e) {
                log.error("代理获取异常");
                return new ProxyClient(retryStrategy, timeout);
            }
        });
    }
}