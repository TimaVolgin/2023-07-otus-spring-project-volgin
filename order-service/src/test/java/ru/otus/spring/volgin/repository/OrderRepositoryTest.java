package ru.otus.spring.volgin.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тест репозитория для работы с заказами")
@DataJpaTest
class OrderRepositoryTest {

    private static final String USER_ID = "36c7648d-1e1f-4dfb-9242-93eaf831c870";
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("Тест поиска списка заказов с полной суммой")
    @Test
    void findAllWithTotalSum() {
        var orders = orderRepository.findAllWithTotalSum(PageRequest.of(0, 10, Sort.by("id")));
        assertEquals(5, orders.getTotalElements(), "Неверное количество записей");
        var content = orders.getContent();
        assertEquals(1, content.get(0).getId(), "Неверный идентификатор записи");
        assertEquals(new BigDecimal(750), content.get(0).getTotalPrice(), "Общая сумма заказов не совпадает");
        assertEquals(new BigDecimal(800), content.get(2).getTotalPrice(), "Общая сумма заказов не совпадает");
        assertEquals(new BigDecimal(1000), content.get(3).getTotalPrice(), "Общая сумма заказов не совпадает");
    }

    @DisplayName("Тест поиска списка заказов с полной суммой")
    @Test
    void findAllByUserId() {
        var orders = orderRepository.findAllByUserId(USER_ID, PageRequest.of(0, 10, Sort.by("id")));
        assertEquals(3, orders.getTotalElements(), "Неверное количество записей");
        var content = orders.getContent();
        assertEquals(3, content.get(0).getId(), "Неверный идентификатор записи");
        assertEquals(new BigDecimal(800), content.get(0).getTotalPrice(), "Общая сумма заказов не совпадает");
        assertEquals(new BigDecimal(1000), content.get(1).getTotalPrice(), "Общая сумма заказов не совпадает");
        assertEquals(new BigDecimal(1200), content.get(2).getTotalPrice(), "Общая сумма заказов не совпадает");
    }

    @DisplayName("Тест поиска заказа для пользователя по идентификатору заказа")
    @Test
    void findByUserIdAndId() {
        var order = orderRepository.findByUserIdAndId(USER_ID, 3);
        assertTrue(order.isPresent(), "Заказ не найден");
        assertEquals(3, order.get().getId(), "Заказ не найден");
        assertEquals(USER_ID, order.get().getUserId(), "Заказ не найден");
        order = orderRepository.findByUserIdAndId(USER_ID, 2);
        assertTrue(order.isEmpty(), "Заказ почему то найден");
    }
}
