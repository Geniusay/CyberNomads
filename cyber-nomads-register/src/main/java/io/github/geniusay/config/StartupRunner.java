package io.github.geniusay.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/15 21:21
 */
@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:9989");  // 可以指定自己的路径
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
