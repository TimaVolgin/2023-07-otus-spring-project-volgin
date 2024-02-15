package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.dto.mappers.OrderMapper;
import ru.otus.spring.volgin.dto.order.CreateOrderRequest;
import ru.otus.spring.volgin.dto.order.OrderDto;
import ru.otus.spring.volgin.dto.order.OrderShortDto;
import ru.otus.spring.volgin.dto.order.UpdateOrderRequest;
import ru.otus.spring.volgin.exceptions.ApplicationException;
import ru.otus.spring.volgin.repository.OrderRepository;

/**
 * Сервис по работе с заказами для администратора
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminOrderService implements OrderService {

    /** Репозиторий для работы с заказами */
    private final OrderRepository orderRepository;
    /** Конвертер заказов */
    private final OrderMapper orderMapper;

    @Override
    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ApplicationException("Не найден заказ"));
        return orderMapper.orderToDtoWithUserInfo(order);
    }

    @Override
    public Page<OrderShortDto> getOrders(Pageable pageable) {
        return orderRepository.findAllWithTotalSum(pageable);
    }

    @Override
    public OrderDto create(CreateOrderRequest createOrderRequest) {
        var order = orderMapper.createRequestToOrder(createOrderRequest, null);
        return orderMapper.orderToDtoWithUserInfo(orderRepository.save(order));
    }

    @Override
    public OrderDto update(UpdateOrderRequest updateOrderRequest) {
        var order = orderRepository.findById(updateOrderRequest.getId())
                .orElseThrow(() -> new ApplicationException("Не найден заказ"));
        orderMapper.updateOrderFromDto(updateOrderRequest, order);
        return orderMapper.orderToDtoWithUserInfo(orderRepository.save(order));
    }
}
