package ru.otus.spring.volgin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.volgin.dto.product.ProductFilter;
import ru.otus.spring.volgin.dto.product.ProductItem;
import ru.otus.spring.volgin.dto.product.ProductItemFull;

import java.util.List;

/**
 * Сервис для работы с продуктами
 */
public interface ProductService {

    /**
     * Возвращает продукты
     * @param productFilter запрос на фильтрацию
     * @param pageable      пагинация
     * @return продукты
     */
    Page<ProductItem> getProducts(ProductFilter productFilter, Pageable pageable);

    /**
     * Возвращает продукт
     * @param id идентификатор продукта
     * @return продукт
     */
    ProductItemFull getProductById(Long id);

    /**
     * Возвращает действующие продукты
     * @param ids идентификаторы продуктов
     * @return продукты
     */
    List<ProductItem> getActiveProductsByIds(List<Long> ids);

    /**
     * Возвращает отключенные продукты
     * @return продукты снятые с витрины
     */
    Page<ProductItem> getDisabled(Pageable pageable);

    /**
     * Снимает продукты с витрины
     * @param ids идентификаторы продуктов
     */
    void disableProducts(List<Long> ids);

    /**
     * Возвращает продукт на витрину
     * @param ids идентификаторы продуктов
     */
    void restoreDisabledProducts(List<Long> ids);

    /**
     * Удаляет продукты
     * @param ids идентификаторы продуктов
     */
    void delete(List<Long> ids);

    /**
     * Возвращает сохранённый/изменённый продукт
     * @param saveProductRequest запрос на изменение/сохранение
     */
    ProductItemFull saveProduct(ProductItemFull saveProductRequest);
}
