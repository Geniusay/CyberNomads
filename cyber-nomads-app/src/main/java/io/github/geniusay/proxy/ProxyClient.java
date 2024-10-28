package io.github.geniusay.proxy;

import lombok.Data;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Data
public class ProxyClient {
    private OkHttpClient client;

    private boolean valid = true;

    public ProxyClient(String ip, Integer port) {
        Proxy proxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress(ip, port));

        this.client = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();
    }
}
