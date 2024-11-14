package io.github.geniusay.core.proxy;

import io.github.geniusay.pojo.DO.Proxy;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Balance {
    Proxy balance(CopyOnWriteArrayList<Proxy> proxies);

    void failCallback(Proxy proxy);

    void successCallback(Proxy proxy);

    void decrement(Proxy proxy);
}
