package ru.otus.spring.volgin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.otus.spring.volgin.domain.Order;
import ru.otus.spring.volgin.dto.mappers.OrderMapper;
import ru.otus.spring.volgin.dto.order.OrderDto;
import ru.otus.spring.volgin.dto.order.OrderShortDto;
import ru.otus.spring.volgin.exceptions.ApplicationException;
import ru.otus.spring.volgin.repository.OrderRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тест сервиса по работе с заказами")
@ExtendWith(MockitoExtension.class)
class UserOrderServiceTest {

    private static final String USER_ID = "test-user-id";

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private UserOrderService userOrderService;

    @BeforeEach
    void init() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(USER_ID, "USER"));
    }

    @DisplayName("Тест получения заказа")
    @Test
    void getOrder() {
        var orderId = 2L;
        var order = new Order();
        when(orderRepository.findByUserIdAndId(eq(USER_ID), eq(orderId))).thenReturn(Optional.of(order));
        var dto = new OrderDto();
        when(orderMapper.orderToDto(order)).thenReturn(dto);
        assertEquals(dto, userOrderService.getOrder(orderId), "Заказ не совпадает");
    }

    @DisplayName("Тест получения несуществующего заказа")
    @Test
    void getNotFoundOrder() {
        when(orderRepository.findByUserIdAndId(anyString(), anyLong())).thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () -> userOrderService.getOrder(2L), "Ожидается ошибка о том что не найден заказ");
    }

    @DisplayName("Тест получения несуществующего заказа")
    @Test
    void getOrders() {
        Page<OrderShortDto> page = Page.empty();
        when(orderRepository.findAllByUserId(eq(USER_ID), any())).thenReturn(page);
        assertEquals(page, userOrderService.getOrders(Pageable.unpaged()), "Заказы не совпадают");
    }

    @DisplayName("Тест получения заказов")
    @Test
    void create() {
        var order = new Order();
        when(orderMapper.createRequestToOrder(any(), eq(USER_ID))).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        var dto = new OrderDto();
        when(orderMapper.orderToDto(order)).thenReturn(dto);
        assertEquals(dto, userOrderService.create(null), "Заказ не совпадает");
    }

    @DisplayName("Тест создания заказа")
    @Test
    void update() {
        assertThrows(UnsupportedOperationException.class, () -> userOrderService.update(null), "Ожидается ошибка о том, что не найден заказ");
    }
}
