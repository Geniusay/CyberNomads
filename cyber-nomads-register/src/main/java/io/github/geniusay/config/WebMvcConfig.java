package io.github.geniusay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/15 21:09
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiPrefixInterceptor());
    }

    @Bean
    public ApiPrefixInterceptor apiPrefixInterceptor() {
        return new ApiPrefixInterceptor();
    }
}
