package ru.otus.spring.volgin.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Продукт
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class ProductItem {

    /** Идентификатор */
    private Long id;
    /** Описание */
    private String description;
    /** Цена */
    private BigDecimal price;
    /** Признак, что продукт снят с витрины */
    private boolean disabled;
}
