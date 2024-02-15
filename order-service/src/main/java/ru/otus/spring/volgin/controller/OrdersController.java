package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.volgin.dto.order.CreateOrderRequest;
import ru.otus.spring.volgin.dto.order.OrderDto;
import ru.otus.spring.volgin.dto.order.OrderShortDto;
import ru.otus.spring.volgin.service.OrderService;

/**
 * Контроллер для работы пользователя с заказами
 */
@Slf4j
@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OrdersController {

    /** Сервис по работе с заказами */
    private final OrderService userOrderService;

    /**
     * Возвращает заказ
     * @param orderId идентификатор заказа
     * @return заказ
     */
    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable Long orderId) {
        return userOrderService.getOrder(orderId);
    }

    /**
     * Возвращает заказы пользователя
     * @return заказ
     */
    @GetMapping("/")
    public Page<OrderShortDto> getOrders(Pageable pageable) {
        return userOrderService.getOrders(pageable);
    }

    /**
     * Создаёт новый заказ
     * @param createOrderRequest запрос на сохранение заказа
     */
    @PostMapping("/")
    public OrderDto save(@RequestBody CreateOrderRequest createOrderRequest) {
        log.info("Создание заказа пользователем {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return userOrderService.create(createOrderRequest);
    }
}
