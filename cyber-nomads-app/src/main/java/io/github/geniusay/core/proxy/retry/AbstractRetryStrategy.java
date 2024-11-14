package io.github.geniusay.core.proxy.retry;

import io.github.geniusay.core.proxy.ProxyClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbstractRetryStrategy {
    public abstract Response execute(Request request, ProxyClient client);
}
