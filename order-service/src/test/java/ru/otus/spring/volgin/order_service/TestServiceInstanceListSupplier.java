package ru.otus.spring.volgin.order_service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * Определяет по какому пути искать в тестах другие сервисы
 */
@RequiredArgsConstructor
public class TestServiceInstanceListSupplier implements ServiceInstanceListSupplier {
    private final Map<String, Integer> services;

    @Override
    public String getServiceId() {
        return null;
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        RequestDataContext context = (RequestDataContext) request.getContext();
        var host = context.getClientRequest().getUrl().getHost();
        return Flux.just(List.of(new DefaultServiceInstance(host, host, "localhost", services.get(host), false)));
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return null;
    }
}
