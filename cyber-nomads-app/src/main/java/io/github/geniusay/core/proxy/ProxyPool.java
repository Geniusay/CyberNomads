package io.github.geniusay.core.proxy;

import io.github.geniusay.constants.RCode;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.mapper.ProxyMapper;
import io.github.geniusay.pojo.DO.Proxy;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProxyPool {
    private final CopyOnWriteArrayList<Proxy> proxies;

    private final Balance balance;

    @Autowired
    public ProxyPool(ProxyMapper proxyMapper) {
        this.proxies = new CopyOnWriteArrayList<>();
        this.proxies.addAll(proxyMapper.selectList(null));
        this.balance = new ProxyPool.WeightedBalance(this.proxies);
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

    protected List<Proxy> getProxies(){
        return this.proxies;
    }

    static class WeightedBalance implements Balance {

        public WeightedBalance(List<Proxy> proxies) {
            this.scores = new PriorityBlockingQueue<>(proxies.size(), new CustomComparator());
            this.proxyMap = new HashMap<>();
            for (Proxy proxy : proxies) {
                ProxyInfo proxyInfo = new ProxyInfo(proxy);
                this.scores.add(proxyInfo);
                this.proxyMap.put(proxy, proxyInfo);
            }
        }

        private final PriorityBlockingQueue<ProxyInfo> scores;

        private final HashMap<Proxy, ProxyInfo> proxyMap;

        @Override
        public Proxy balance(CopyOnWriteArrayList<Proxy> proxies) {
            if(scores.isEmpty()){
                throw new ServeException(RCode.PROXY_BALANCE_ERROR);
            }
            ProxyInfo proxyInfo = scores.poll();
            proxyInfo.choose();
            synchronized (proxyInfo) {
                scores.remove(proxyInfo);
                scores.add(proxyInfo);
            }
            return proxyInfo.getProxy();
        }

        @Override
        public void failCallback(Proxy proxy) {
            ProxyInfo proxyInfo = proxyMap.get(proxy);
            proxyInfo.failCallback();
            synchronized (proxyInfo) {
                scores.remove(proxyInfo);
                scores.add(proxyInfo);
            }
        }

        @Override
        public void successCallback(Proxy proxy) {
            ProxyInfo proxyInfo = proxyMap.get(proxy);
            proxyInfo.successCallback();
            synchronized (proxyInfo) {
                scores.remove(proxyInfo);
                scores.add(proxyInfo);
            }
        }


        static class ProxyInfo {

            public ProxyInfo(Proxy proxy) {
                this.total = new AtomicInteger(0);
                this.failed = new AtomicInteger(0);
                this.using = new AtomicInteger(0);
                this.continueFailedTimes = new AtomicInteger(0);
                this.score = new AtomicInteger(100);
                this.proxy = proxy;
            }

            /*
            * 总数
            * */
            private final AtomicInteger total;
            /*
            * 失败的次数
            * */
            private final AtomicInteger failed;
            /*
            * 正在使用的人数
            * */
            private final AtomicInteger using;

            private final AtomicInteger continueFailedTimes;

            @Getter
            private Proxy proxy;

            private static final Integer MAX_FAILED_TIMES = 3;
            /*
            * 代理总分数= 200
            * 失败一次 分数减
            * */
            private final AtomicInteger score;

            protected void choose(){
                this.using.incrementAndGet();
                this.total.incrementAndGet();
                int score = -5;
                updateScore(score);
            }

            protected void failCallback(){
                this.using.decrementAndGet();
                this.failed.incrementAndGet();
                int times = this.continueFailedTimes.incrementAndGet(), score = (int) (-10 * this.failed.doubleValue() / this.total.doubleValue());
                // 连续失败次数过多
                if(times >= MAX_FAILED_TIMES) score -= 20;
                score += 5;
                updateScore(score);
            }

            protected void successCallback(){
                this.using.decrementAndGet();
                // 自旋cas
                while(!this.continueFailedTimes.compareAndSet(this.continueFailedTimes.get(), 0));
                int score = (int) (10 * this.failed.doubleValue() / this.total.doubleValue()) + 2;
                score += 5;
                updateScore(score);
            }

            protected void updateScore(int score){
                this.score.addAndGet(score);
            }

            public int getScore(){
                return this.score.get();
            }
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
    }
}
