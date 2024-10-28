package io.github.geniusay.proxy;

import io.github.geniusay.pojo.Proxy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyManager {
    private final CopyOnWriteArrayList<ProxyClient> proxyPool;

    private final BalanceStrategy balance;

    public ProxyManager() {
        this.proxyPool = new CopyOnWriteArrayList<>();
        this.balance = new ProxyManager.RoundRobinBalance();
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

    public java.net.Proxy getProxy(){
        if(this.proxyPool.isEmpty()) {
            throw new RuntimeException("代理池为空");
        }
        return balance.balance(this.proxyPool);
    }

    // 轮询
    static class RoundRobinBalance implements BalanceStrategy {
        private AtomicInteger index = new AtomicInteger(0);

        @Override
        public java.net.Proxy balance(CopyOnWriteArrayList<ProxyClient> proxyPool) {
            int size = proxyPool.size(), newIndex = index.getAndIncrement() % size;
            ProxyClient proxyClient;
            while((proxyClient = proxyPool.get(newIndex)) == null || !proxyClient.isValid()){
                size = proxyPool.size();
                newIndex = index.getAndIncrement() % size;
            }
            return proxyClient.getProxy();
        }
    }
    // 随机
    static class RandomBalance implements BalanceStrategy {
        @Override
        public java.net.Proxy balance(CopyOnWriteArrayList<ProxyClient> proxyPool) {
            return proxyPool.get(ThreadLocalRandom.current().nextInt(proxyPool.size())).getProxy();
        }
    }
}
