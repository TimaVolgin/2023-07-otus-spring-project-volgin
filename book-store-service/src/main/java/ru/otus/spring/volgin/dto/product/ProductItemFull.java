package ru.otus.spring.volgin.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Продукт
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItemFull {

    /** Идентификатор */
    private Long id;
    /** Описание */
    private String description;
    /** Цена */
    private BigDecimal price;
    /** Признак, что продукт снят с витрины */
    private boolean disabled;
    /** Список идентификаторов книг */
    private List<Long> books;
}
