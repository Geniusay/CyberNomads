package io.github.geniusay.pojo.DTO;

import lombok.Data;

@Data
public class WbiSignatureResult {
    private final long wts;
    private final String w_rid;

    public WbiSignatureResult(long wts, String w_rid) {
        this.wts = wts;
        this.w_rid = w_rid;
    }
}