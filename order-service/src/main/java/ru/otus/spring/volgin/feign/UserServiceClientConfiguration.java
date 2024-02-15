package ru.otus.spring.volgin.feign;

import com.google.common.net.HttpHeaders;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class UserServiceClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                    .getRequest()
                    .getHeader(HttpHeaders.AUTHORIZATION);
            requestTemplate.header(HttpHeaders.AUTHORIZATION, token);
        };
    }
}
