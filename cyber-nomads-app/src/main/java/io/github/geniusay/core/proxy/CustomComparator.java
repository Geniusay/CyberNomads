package io.github.geniusay.core.proxy;

import java.util.Comparator;

class CustomComparator implements Comparator<ProxyPool.WeightedBalance.ProxyInfo> {
    @Override
    public int compare(ProxyPool.WeightedBalance.ProxyInfo o1, ProxyPool.WeightedBalance.ProxyInfo o2) {
        return o2.getScore() - o1.getScore();
    }
}