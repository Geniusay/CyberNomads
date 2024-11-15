package io.github.geniusay.core.proxy.retry;

import io.github.geniusay.constants.RCode;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.proxy.ProxyClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

@Slf4j
public class TimesRetryStrategy extends AbstractRetryStrategy{
    /*
    * 重试次数
    * */
    private final Integer times;

    public TimesRetryStrategy() {
        this.times = 2;
    }

    public TimesRetryStrategy(Integer times) {
        this.times = times;
    }

    @Override
    public Response execute(Request request, ProxyClient proxyClient) {
        for (int i = 0; i < times; i++) {
            try (Response res = proxyClient.getClient().newCall(request).execute()) {
                return res;
            } catch (SocketTimeoutException | SocketException ignored) {

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.error("代理失效 ip:{},port:{}", proxyClient.getProxy().getIp(), proxyClient.getProxy().getPort());
        return null;
    }
}
