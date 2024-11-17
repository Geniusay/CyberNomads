package io.github.geniusay.core.proxy;

import io.github.geniusay.constants.RCode;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.proxy.retry.AbstractRetryStrategy;
import lombok.Data;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Data
public class ProxyClient {
    private static final Logger log = LoggerFactory.getLogger(ProxyClient.class);
    private io.github.geniusay.pojo.DO.Proxy proxy;

    private OkHttpClient client;

    private AbstractRetryStrategy retryStrategy;

    private Integer timeout;

    private ProxyPool proxyPool;

    public ProxyClient(AbstractRetryStrategy retryStrategy, io.github.geniusay.pojo.DO.Proxy proxy, Integer timeout, ProxyPool proxyPool) {
        this.retryStrategy = retryStrategy;
        this.proxy = proxy;
        this.client = new OkHttpClient.Builder()
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxy.getIp(), proxy.getPort())))
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        this.timeout = timeout;
        this.proxyPool = proxyPool;
    }

    public ProxyClient(AbstractRetryStrategy retryStrategy, Integer timeout, ProxyPool proxyPool) {
        this.retryStrategy = retryStrategy;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        this.timeout = timeout;
        this.proxyPool = proxyPool;
    }

    public ProxyClient(AbstractRetryStrategy retryStrategy, io.github.geniusay.pojo.DO.Proxy proxy, ProxyPool proxyPool) {
        this.retryStrategy = retryStrategy;
        this.proxy = proxy;
        this.client = new OkHttpClient.Builder()
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxy.getIp(), proxy.getPort())))
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        this.timeout = 4;
        this.proxyPool = proxyPool;
    }

    public ProxyClient(AbstractRetryStrategy retryStrategy, ProxyPool proxyPool) {
        this.retryStrategy = retryStrategy;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        this.timeout = 3;
        this.proxyPool = proxyPool;
    }

    public Response call(Request request) {
        // 1、正常执行
        try (Response res = this.client.newCall(request).execute()) {
            proxyPool.successCallback(this.proxy);
            return res;
        } catch (SocketTimeoutException | SocketException e) {
            if (!Objects.equals(retryStrategy, null)){
                // 2、超时失败重试
                Response res = retryStrategy.execute(request, this);
                if (res != null){
                    return res;
                }
            }
            // 3、发现重试后代理依然不可用，于是告诉代理池自己的不满
            proxyPool.failCallback(this.proxy);
            // 4、切换代理
            nextProxy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void nextProxy(){
        setProxy(proxyPool.getProxy());
        setClient(new OkHttpClient.Builder()
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxy.getIp(), proxy.getPort())))
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build());
    }
}