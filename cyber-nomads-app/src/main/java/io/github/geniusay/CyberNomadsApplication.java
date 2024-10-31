package io.github.geniusay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CyberNomadsApplication {
    public static void main( String[] args )
    {
        SpringApplication.run(CyberNomadsApplication.class, args);
    }
}
