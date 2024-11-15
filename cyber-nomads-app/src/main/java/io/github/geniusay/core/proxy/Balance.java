package io.github.geniusay.core.proxy;

import io.github.geniusay.pojo.DO.Proxy;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Balance {
    Proxy balance(CopyOnWriteArrayList<Proxy> proxies);
    /*
    * 对代理失败的不满
    * */
    void failCallback(Proxy proxy);
    /*
    * 对代理成功的称赞
    * */
    void successCallback(Proxy proxy);
}
