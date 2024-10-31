package io.github.geniusay.utils;

import io.github.geniusay.proxy.ProxyClient;
import io.github.geniusay.proxy.ProxyManager;
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

    public <K, V> String get(String url, Map<K, V> headers, Map<K, V>  params){
        return get(transToHttpUrl(url, params), transToHeaders(headers));
    }

    public String get(String url){
        return execute(new Request.Builder()
                .url(url)
                .get()
                .build());
    }

    public String get(HttpUrl url, Headers headers){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build());
    }

    public String post(String url, Headers headers, String body, MediaType type){
        return post(url, headers, RequestBody.create(type, body));
    }

    public String post(String url, Headers headers, RequestBody requestBody){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build());
    }

    public String put(String url, Headers headers, String body, MediaType type){
        return post(url, headers, RequestBody.create(type, body));
    }

    public String put(String url, Headers headers, RequestBody requestBody){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .put(requestBody)
                .build());
    }

    public <K, V> String delete(String url, Map<K, V> headers, Map<K, V> params){
        return delete(transToHttpUrl(url, params), transToHeaders(headers));
    }

    public String delete(HttpUrl url, Headers headers){
        return execute(new Request.Builder()
                .url(url)
                .headers(headers)
                .delete()
                .build());
    }

    public String execute(Request request){
        Callable<String> callable = () -> {
            OkHttpClient client = getClient();
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
            String uid = ThreadUtil.getUid();
            nextClient();
            log.info("{}请求失败，代理切换", uid);
            throw new RuntimeException(e);
        }
    }

    public OkHttpClient getClient(){
        String uid = ThreadUtil.getUid();
        return proxyCache.computeIfAbsent(uid, k -> proxyManager.getClient()).getClient();
    }

    private void nextClient(){
        String uid = ThreadUtil.getUid();
        if(!proxyCache.containsKey(uid)) return;
        addRetryTask(proxyCache.get(uid));
        proxyManager.unavailableProxies(proxyCache.get(uid));
        proxyCache.put(uid, proxyManager.getClient());
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
