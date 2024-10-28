package io.github.geniusay.proxy;

import java.util.concurrent.CopyOnWriteArrayList;

public interface BalanceStrategy {
    ProxyClient balance(CopyOnWriteArrayList<ProxyClient> proxyPool);
}
