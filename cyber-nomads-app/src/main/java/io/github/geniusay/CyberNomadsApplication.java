package io.github.geniusay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@MapperScan("io.github.geniusay.mapper")
@EnableAsync
@EnableScheduling
public class CyberNomadsApplication {
    public static void main( String[] args )
    {
        SpringApplication.run(CyberNomadsApplication.class, args);
    }
}
