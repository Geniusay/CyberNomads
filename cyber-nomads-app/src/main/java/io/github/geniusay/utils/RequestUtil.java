package io.github.geniusay.utils;

import io.github.geniusay.core.proxy.ProxyClient;
import io.github.geniusay.core.proxy.ProxyManager;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

@Component
public class RequestUtil {
    private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);
    @Resource
    private ProxyManager proxyManager;

    private final Timer timer = new Timer();

    @Resource
    ThreadPoolExecutor requestThreadPool;

    private final Map<String, ProxyClient> proxyCache = new ConcurrentHashMap<>();

    public <K, V> String get(String url, Map<K, V> headers, Map<K, V>  params, String robotId){
        return get(transToHttpUrl(url, params), transToHeaders(headers), robotId);
    }

    public String get(String url, String robotId) {
        return execute(new Request.Builder()
                .url(url)
                .get()
                .build(), robotId);
    }

    public String get(HttpUrl url, Headers headers, String robotId){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build(), robotId);
    }

    public String post(String url, Headers headers, String body, MediaType type, String robotId){
        return post(url, headers, RequestBody.create(type, body), robotId);
    }

    public String post(String url, Headers headers, RequestBody requestBody, String robotId){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build(), robotId);
    }

    public String put(String url, Headers headers, String body, MediaType type, String robotId){
        return post(url, headers, RequestBody.create(type, body), robotId);
    }

    public String put(String url, Headers headers, RequestBody requestBody, String robotId){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .put(requestBody)
                .build(), robotId);
    }

    public <K, V> String delete(String url, Map<K, V> headers, Map<K, V> params, String robotId){
        return delete(transToHttpUrl(url, params), transToHeaders(headers), robotId);
    }

    public String delete(HttpUrl url, Headers headers, String robotId){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .delete()
                .build(), robotId);
    }

    public String execute(Request request, String robotId){
        Callable<String> callable = () -> {
            OkHttpClient client = getClient(robotId);
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        try {
            Future<String> future = requestThreadPool.submit(callable);
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            nextClient(robotId);
            log.info("{}请求失败，代理切换", robotId);
            throw new RuntimeException(e);
        }
    }

    public OkHttpClient getClient(String robotId){
        log.info("代理获取{}", robotId);
        return proxyCache.computeIfAbsent(robotId, k -> proxyManager.getClient()).getClient();
    }

    private void nextClient(String robotId){
        if(!proxyCache.containsKey(robotId)) return;
        addRetryTask(proxyCache.get(robotId));
        proxyManager.unavailableProxies(proxyCache.get(robotId));
        proxyCache.put(robotId, proxyManager.getClient());
    }

    private void addRetryTask(ProxyClient client){
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isProxyAvailable(client)){
                    proxyManager.availableProxies(client);
                } else {
                    addRetryTask(client);
                }
            }
        }, 20000);
    }

    public boolean isProxyAvailable(ProxyClient client) {
        HttpUrl url = HttpUrl.get("http://119.3.234.15:9000/retry");

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.getClient().newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <K, V> Headers transToHeaders(Map<K, V> headers){
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headers != null && !headers.isEmpty()){
            for(Map.Entry<K, V> entry : headers.entrySet()){
                headersBuilder.add(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return headersBuilder.build();
    }

    private <K, V> HttpUrl transToHttpUrl(String url, Map<K, V> params){
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if(params != null && !params.isEmpty()){
            for(Map.Entry<K, V> entry : params.entrySet()){
                urlBuilder.addQueryParameter(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return  urlBuilder.build();
    }
}
