package io.github.geniusay.core.proxy;

import io.github.geniusay.constants.RCode;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.proxy.retry.AbstractRetryStrategy;
import lombok.Data;
import okhttp3.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

@Data
public class ProxyClient {
    private io.github.geniusay.pojo.DO.Proxy proxy;

    private OkHttpClient client;

    private AbstractRetryStrategy retryStrategy;

    private Integer timeout;

    private static ProxyPool proxyPool;

    @Resource
    public void setProxyPool(ProxyPool proxyPool){
        ProxyClient.proxyPool = proxyPool;
    }

    public ProxyClient(AbstractRetryStrategy retryStrategy, io.github.geniusay.pojo.DO.Proxy proxy, Integer timeout) {
        this.retryStrategy = retryStrategy;
        this.proxy = proxy;
        this.client = new OkHttpClient.Builder()
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxy.getIp(), proxy.getPort())))
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        this.timeout = timeout;
    }

    public ProxyClient(AbstractRetryStrategy retryStrategy, Integer timeout) {
        this.retryStrategy = retryStrategy;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        this.timeout = timeout;
    }

    public ProxyClient(AbstractRetryStrategy retryStrategy, io.github.geniusay.pojo.DO.Proxy proxy) {
        this.retryStrategy = retryStrategy;
        this.proxy = proxy;
        this.client = new OkHttpClient.Builder()
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxy.getIp(), proxy.getPort())))
                .build();
        this.timeout = 3;
    }

    public ProxyClient(AbstractRetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
        this.client = new OkHttpClient.Builder()
                .build();
        this.timeout = 3;
    }

    public Response call(Request request) {
        try (Response res = this.client.newCall(request).execute()) {
            proxyPool.successCallback(this.proxy);
            return res;
        } catch (SocketTimeoutException e) {
            Response res = retryStrategy.execute(request, this);
            if (res != null){
                return res;
            }
            proxyPool.failCallback(this.proxy);

            throw new ServeException(RCode.PROXY_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void nextProxy(){
        proxyPool.decrement(this.proxy);
        setProxy(proxyPool.getProxy());
        setClient(new OkHttpClient.Builder()
                .proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxy.getIp(), proxy.getPort())))
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build());
    }
}