package io.github.geniusay.crawler.util.bilibili;

/**
 * 自定义解析器接口，用于处理非标准的 JSON 数据
 */
public interface ResponseParser<T> {
    T parse(String response);
}