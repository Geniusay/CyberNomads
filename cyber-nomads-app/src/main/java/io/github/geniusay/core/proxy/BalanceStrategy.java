package io.github.geniusay.core.proxy;

import java.util.concurrent.CopyOnWriteArrayList;

public interface BalanceStrategy {
    ProxyClient balance(CopyOnWriteArrayList<ProxyClient> proxyPool);
}
