package io.github.geniusay.proxy;

import java.net.Proxy;
import java.util.concurrent.CopyOnWriteArrayList;

public interface BalanceStrategy {
    Proxy balance(CopyOnWriteArrayList<ProxyClient> proxyPool);
}
