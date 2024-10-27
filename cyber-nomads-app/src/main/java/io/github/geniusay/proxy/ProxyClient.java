package io.github.geniusay.proxy;

import lombok.Data;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Data
public class ProxyClient {
    private Proxy proxy;

    private boolean valid = true;

    public ProxyClient(String ip, Integer port) {
        this.proxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress(ip, port));
    }
}
