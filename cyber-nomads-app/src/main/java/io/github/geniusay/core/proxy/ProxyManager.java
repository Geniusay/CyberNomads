package io.github.geniusay.core.proxy;

import io.github.geniusay.mapper.ProxyMapper;
import io.github.geniusay.pojo.DO.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProxyManager {
    private final CopyOnWriteArrayList<ProxyClient> proxyPool;

    private volatile AtomicInteger availableProxiesNum;

    private final BalanceStrategy balance;

    private final Object lock = new Object();

    @Autowired
    public ProxyManager(ProxyMapper proxyMapper) {
        this.proxyPool = new CopyOnWriteArrayList<>();
        this.balance = new ProxyManager.RoundRobinBalance();
        List<Proxy> proxies = proxyMapper.selectList(null);
        ArrayList<ProxyClient> list = new ArrayList<>();
        for (Proxy proxy : proxies){
            list.add(new ProxyClient(proxy.getIp(), proxy.getPort()));
        }
        this.proxyPool.addAll(list);
        this.availableProxiesNum = new AtomicInteger(this.proxyPool.size());
    }

    public ProxyManager(List<Proxy> proxies){
        ArrayList<ProxyClient> list = new ArrayList<>();
        for (Proxy proxy : proxies){
            list.add(new ProxyClient(proxy.getIp(), proxy.getPort()));
        }
        this.proxyPool = new CopyOnWriteArrayList<>(list);
        this.balance = new ProxyManager.RoundRobinBalance();
    }

    public ProxyManager(List<Proxy> proxies, BalanceStrategy balance){
        ArrayList<ProxyClient> list = new ArrayList<>();
        for (Proxy proxy : proxies){
            list.add(new ProxyClient(proxy.getIp(), proxy.getPort()));
        }
        this.proxyPool = new CopyOnWriteArrayList<>(list);
        this.balance = balance;
    }

    public void addProxy(ProxyClient proxy) {

        proxyPool.add(proxy);
    }

    public void addProxy(Proxy proxy) {
        proxyPool.add(new ProxyClient(proxy.getIp(), proxy.getPort()));
    }

    public void removeProxy(ProxyClient proxy) {
        proxyPool.remove(proxy);
    }

    public void addAll(List<Proxy> proxies) {
        ArrayList<ProxyClient> list = new ArrayList<>();
        for (Proxy proxy : proxies){
            list.add(new ProxyClient(proxy.getIp(), proxy.getPort()));
        }
        proxyPool.addAll(list);
    }

    public void removeAll(List<ProxyClient> proxies) {
        proxyPool.removeAll(proxies);
    }

    public void clear(){
        proxyPool.clear();
    }

    public ProxyClient getClient(){
        if(this.proxyPool.isEmpty()) {
            throw new RuntimeException("代理池为空");
        }
        return balance.balance(this.proxyPool);
    }

    public void unavailableProxies(ProxyClient proxy){
        if(proxy.isValid()){
            this.availableProxiesNum.decrementAndGet();
            proxy.setValid(false);
        }
    }

    public void availableProxies(ProxyClient proxy){
        this.availableProxiesNum.incrementAndGet();
        proxy.setValid(true);
    }

    public boolean isAvailable(){
        return this.availableProxiesNum.get() > 0;
    }

    // 轮询
    static class RoundRobinBalance implements BalanceStrategy {
        private AtomicInteger index = new AtomicInteger(0);

        @Override
        public ProxyClient balance(CopyOnWriteArrayList<ProxyClient> proxyPool) {
            int size = proxyPool.size(), newIndex = index.getAndIncrement() % size;
            ProxyClient proxyClient;
            while((proxyClient = proxyPool.get(newIndex)) == null || !proxyClient.isValid()){
                size = proxyPool.size();
                newIndex = index.getAndIncrement() % size;
            }
            return proxyClient;
        }
    }
    // 随机
    static class RandomBalance implements BalanceStrategy {
        @Override
        public ProxyClient balance(CopyOnWriteArrayList<ProxyClient> proxyPool) {
            return proxyPool.get(ThreadLocalRandom.current().nextInt(proxyPool.size()));
        }
    }
}
