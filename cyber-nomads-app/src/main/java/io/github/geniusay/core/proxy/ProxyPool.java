package io.github.geniusay.core.proxy;

import io.github.geniusay.mapper.ProxyMapper;
import io.github.geniusay.pojo.DO.Proxy;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProxyPool {
    private final CopyOnWriteArrayList<Proxy> proxies;

    private final Balance balance;

    @Autowired
    public ProxyPool(ProxyMapper proxyMapper) {
        this.proxies = new CopyOnWriteArrayList<>();
        this.balance = new ProxyPool.RoundRobinBalance();
        this.proxies.addAll(proxyMapper.selectList(null));
    }

    public Proxy getProxy(){
        if(this.proxies.isEmpty()) {
            throw new RuntimeException("代理池为空");
        }
        return balance.balance(this.proxies);
    }

    /*
    * 代理请求失败
    * */
    public void failCallback(Proxy proxy){
        this.balance.failCallback(proxy);
    }

    /*
    * 代理请求成功
    * */
    public void successCallback(Proxy proxy){
        this.balance.successCallback(proxy);
    }

    public void decrement(Proxy proxy){

    }
    static class WeightedBalance implements Balance {
        private final ConcurrentHashMap<Proxy, ProxyInfo> proxiesInfo = new ConcurrentHashMap<>();

        @Override
        public Proxy balance(CopyOnWriteArrayList<Proxy> proxies) {
            return null;
        }

        @Override
        public void failCallback(Proxy proxy) {

        }

        @Override
        public void successCallback(Proxy proxy) {

        }

        @Override
        public void decrement(Proxy proxy) {

        }

        @Data
        @Builder
        static class ProxyInfo {
            private AtomicInteger total;

            private AtomicInteger failed;
        }
    }

    // 轮询
    static class RoundRobinBalance implements Balance {
        private final AtomicInteger index = new AtomicInteger(0);

        @Override
        public Proxy balance(CopyOnWriteArrayList<Proxy> proxies) {
            int size, newIndex;
            Proxy proxy;
            do {
                size = proxies.size();
                newIndex = index.getAndIncrement() % size;
            }while ((proxy = proxies.get(newIndex)) == null);
            return proxy;
        }

        @Override
        public void failCallback(Proxy proxy) {

        }

        @Override
        public void successCallback(Proxy proxy) {

        }

        @Override
        public void decrement(Proxy proxy) {

        }
    }
}
