//package io.github.geniusay.core.proxy;
//
//import io.github.geniusay.core.proxy.retry.TimesRetryStrategy;
//import io.github.geniusay.pojo.DO.Proxy;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.List;
//
//@Slf4j
//@Component
//public class ProxySentry {
//
//    @Resource
//    private ProxyPool proxyPool;
//
//    private Request request;
//
//    @PostConstruct
//    public void init() {
//        request = new Request.Builder().url("http://www.baidu.com").build();
//    }
//
//    @Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
//    void proxyScore() {
//        List<Proxy> proxies = proxyPool.getProxies();
//        for (Proxy proxy : proxies) {
//            ProxyClient client = new ProxyClient(null, proxy, proxyPool);
//            Response res = client.call(request);
//            if (res.isSuccessful()) {
//                proxyPool.successCallback(proxy);
//            } else {
//                proxyPool.failCallback(proxy);
//            }
//        }
//    }
//}
