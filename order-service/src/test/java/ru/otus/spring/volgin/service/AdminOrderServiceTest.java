package ru.otus.spring.volgin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.volgin.domain.Order;
import ru.otus.spring.volgin.dto.mappers.OrderMapper;
import ru.otus.spring.volgin.dto.order.OrderDto;
import ru.otus.spring.volgin.dto.order.OrderShortDto;
import ru.otus.spring.volgin.dto.order.UpdateOrderRequest;
import ru.otus.spring.volgin.exceptions.ApplicationException;
import ru.otus.spring.volgin.repository.OrderRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тест сервиса по работе с заказами")
@ExtendWith(MockitoExtension.class)
class AdminOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private AdminOrderService adminOrderService;

    @DisplayName("Тест получения заказа")
    @Test
    void getOrder() {
        var orderId = 2L;
        var order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        var dto = new OrderDto();
        when(orderMapper.orderToDtoWithUserInfo(order)).thenReturn(dto);
        assertEquals(dto, adminOrderService.getOrder(orderId), "Заказ не совпадает");
    }

    @DisplayName("Тест получения несуществующего заказа")
    @Test
    void getNotFoundOrder() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () -> adminOrderService.getOrder(2L), "Ожидается ошибка о том что не найден заказ");
    }

    @DisplayName("Тест получения заказов")
    @Test
    void getOrders() {
        Page<OrderShortDto> page = Page.empty();
        when(orderRepository.findAllWithTotalSum(any())).thenReturn(page);
        assertEquals(page, adminOrderService.getOrders(Pageable.unpaged()), "Заказы не совпадают");
    }

    @DisplayName("Тест создания заказа")
    @Test
    void create() {
        var order = new Order();
        when(orderMapper.createRequestToOrder(any(), any())).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        var dto = new OrderDto();
        when(orderMapper.orderToDtoWithUserInfo(any())).thenReturn(dto);
        assertEquals(dto, adminOrderService.create(null), "Заказ не совпадает");
    }

    @DisplayName("Тест обновления заказа")
    @Test
    void update() {
        var order = new Order();
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        var dto = new OrderDto();
        when(orderMapper.orderToDtoWithUserInfo(any())).thenReturn(dto);
        assertEquals(dto, adminOrderService.update(new UpdateOrderRequest()), "Заказ не совпадает");
        verify(orderMapper).updateOrderFromDto(any(), eq(order));
    }

    @DisplayName("Тест обновления заказа которого не существует")
    @Test
    void updateNotFound() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () -> adminOrderService.update(new UpdateOrderRequest()), "Ожидается ошибка о том, что не найден заказ");
    }
}
