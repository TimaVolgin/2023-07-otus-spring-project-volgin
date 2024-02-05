package ru.otus.spring.volgin.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Фильтр на получение продукта
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductFilter {
    /** Признак того что продукт снят с витрины */
    private Boolean disabled;
    /** Идентификатор книги для поиска продуктов, которые содержат данную книгу */
    private Long bookId;

}
