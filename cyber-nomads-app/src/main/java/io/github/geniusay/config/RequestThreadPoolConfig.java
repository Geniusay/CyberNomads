package io.github.geniusay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class RequestThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor requestThreadPool(){
        return new ThreadPoolExecutor(10,
                                    20,
                                    60,
                                    TimeUnit.SECONDS,
                                    new ArrayBlockingQueue<>(100)
                                    , new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
