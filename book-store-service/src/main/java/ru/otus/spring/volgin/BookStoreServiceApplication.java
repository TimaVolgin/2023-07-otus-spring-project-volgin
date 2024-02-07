package ru.otus.spring.volgin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Приложение по работе с магазином книг
 */
@EnableDiscoveryClient
@SpringBootApplication
public class BookStoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreServiceApplication.class, args);
    }
}
