package ru.otus.spring.volgin.dto.mappers;

import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.spring.volgin.domain.OrderItem;
import ru.otus.spring.volgin.dto.product.ProductItem;

/**
 * Конвертер продуктов
 */
@AllArgsConstructor
@Mapper(componentModel = "spring")
public abstract class ProductToOrderMapper {

    /**
     * Конвертирует продукт в продукт заказа
     * @param productItem продукт
     * @return продукт заказа
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "productItem.id")
    public abstract OrderItem map(ProductItem productItem, int count);
}
