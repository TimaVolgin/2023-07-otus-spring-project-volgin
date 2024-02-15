package ru.otus.spring.volgin.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Продукты заказа
 */
@AllArgsConstructor
@Setter
@Getter
public class OrderItemDto {

    /** Идентификатор */
    private Long id;
    /** Идентификатор товара */
    private Long productId;
    /** Заголовок товара */
    private String description;
    /** Цена */
    private BigDecimal price;
    /** Количество */
    private int count;
}
