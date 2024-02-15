package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.dto.mappers.OrderMapper;
import ru.otus.spring.volgin.dto.order.CreateOrderRequest;
import ru.otus.spring.volgin.dto.order.OrderDto;
import ru.otus.spring.volgin.dto.order.OrderShortDto;
import ru.otus.spring.volgin.dto.order.UpdateOrderRequest;
import ru.otus.spring.volgin.exceptions.ApplicationException;
import ru.otus.spring.volgin.repository.OrderRepository;

/**
 * Сервис по работе с заказами от лица пользователя
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderService implements OrderService {

    /** Репозиторий для работы с заказами */
    private final OrderRepository orderRepository;
    /** Конвертер заказов */
    private final OrderMapper orderMapper;

    @Override
    public OrderDto getOrder(Long orderId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var order = orderRepository.findByUserIdAndId(userId, orderId)
                .orElseThrow(() -> new ApplicationException("Не найден заказ"));
        return orderMapper.orderToDto(order);
    }

    @Override
    public Page<OrderShortDto> getOrders(Pageable pageable) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public OrderDto create(CreateOrderRequest createOrderRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var order = orderMapper.createRequestToOrder(createOrderRequest, userId);
        return orderMapper.orderToDto(orderRepository.save(order));
    }

    @Override
    public OrderDto update(UpdateOrderRequest updateOrderRequest) {
        throw new UnsupportedOperationException("Данное действие пользователю не доступно");
    }
}
