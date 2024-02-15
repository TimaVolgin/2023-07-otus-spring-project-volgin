package ru.otus.spring.volgin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.volgin.dto.order.CreateOrderRequest;
import ru.otus.spring.volgin.dto.order.OrderDto;
import ru.otus.spring.volgin.dto.order.OrderShortDto;
import ru.otus.spring.volgin.dto.order.UpdateOrderRequest;

/**
 * Сервис по работе с заказами
 */
public interface OrderService {

    /**
     * Возвращает заказ
     * @param orderId идентификатор заказа
     * @return заказ
     */
    OrderDto getOrder(Long orderId);

    /**
     * Возвращает заказы пользователя
     * @param pageable пагинация
     * @return заказ
     */
    Page<OrderShortDto> getOrders(Pageable pageable);

    /**
     * Создаёт новый заказ
     * @param createOrderRequest запрос на сохранение заказа
     * @return созданный заказ
     */
    OrderDto create(CreateOrderRequest createOrderRequest);

    /**
     * Изменяет заказ
     * @param updateOrderRequest запрос на изменения заказа
     * @return измененный заказ
     */
    OrderDto update(UpdateOrderRequest updateOrderRequest);
}
