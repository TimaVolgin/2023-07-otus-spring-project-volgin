package ru.otus.spring.volgin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.volgin.domain.Product;
import ru.otus.spring.volgin.dto.mappers.ProductMapperImpl;
import ru.otus.spring.volgin.dto.product.ProductFilter;
import ru.otus.spring.volgin.dto.product.ProductItemFull;
import ru.otus.spring.volgin.exceptions.ApplicationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Интеграционный тест сервиса для работы с продуктом")
@DataJpaTest
@Import({ProductServiceImpl.class, ProductMapperImpl.class})
class ProductServiceImplTest {

    private static final Long DISABLED_PRODUCT_ID = 4L;

    @Autowired
    private ProductService productService;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Тест получения продуктов")
    @Test
    void getProducts() {
        var filter = new ProductFilter();
        var products = productService.getProducts(filter, Pageable.ofSize(5));
        assertEquals(10, products.getTotalElements(), "Неверное количество элементов");
        assertEquals(2, products.getTotalPages(), "Неверное количество страниц");

        filter.setBookId(1L);
        products = productService.getProducts(filter, Pageable.ofSize(6));
        assertEquals(2, products.getTotalElements(), "Неверное количество элементов");
        assertEquals(1, products.getTotalPages(), "Неверное количество страниц");

        filter.setDisabled(false);
        products = productService.getProducts(filter, Pageable.ofSize(5));
        assertEquals(2, products.getTotalElements(), "Неверное количество элементов");
        assertEquals(1, products.getTotalPages(), "Неверное количество страниц");

        filter.setDisabled(true);
        products = productService.getProducts(filter, Pageable.ofSize(5));
        assertEquals(0, products.getTotalElements(), "Неверное количество элементов");
        assertEquals(0, products.getTotalPages(), "Неверное количество страниц");

        filter.setBookId(DISABLED_PRODUCT_ID);
        filter.setDisabled(true);
        products = productService.getProducts(filter, Pageable.ofSize(5));
        assertEquals(1, products.getTotalElements(), "Неверное количество элементов");
        assertEquals(1, products.getTotalPages(), "Неверное количество страниц");
    }

    @DisplayName("Тест получения продукта по идентификатору")
    @Test
    void getProductById() {
        var product = productService.getProductById(9L);
        assertEquals(9L, product.getId());
        assertEquals("Избранные сочинения Толстого", product.getDescription());
        assertEquals(new BigDecimal(700), product.getPrice());
        assertEquals(2, product.getBooks().size());
    }

    @DisplayName("Тест получения активных продуктов по идентификаторам")
    @Test
    void getActiveProductsByIds() {
        var result = productService.getActiveProductsByIds(List.of(
                DISABLED_PRODUCT_ID,
                1L, 2L, 3L
        ));
        assertEquals(3, result.size());
        assertEquals(0, result.stream()
                .filter(product -> Objects.equals(DISABLED_PRODUCT_ID, product.getId()))
                .count());
    }

    @DisplayName("Тест получения снятых с витрины продуктов")
    @Test
    void getDisabledProducts() {
        var disabled = productService.getDisabled(Pageable.unpaged());
        assertEquals(1, disabled.getTotalElements());
        assertEquals(DISABLED_PRODUCT_ID, disabled.getContent().get(0).getId());
    }

    @DisplayName("Тест отключения продукта")
    @Test
    void disableProducts() {
        var productId = 8L;
        assertFalse(em.find(Product.class, productId).isDisabled(), "Продукт не должен быть отключен");
        productService.disableProducts(List.of(productId));
        em.clear();
        assertTrue(em.find(Product.class, productId).isDisabled(), "Продукт не был снят с витрины");
    }

    @DisplayName("Тест восстановления отключенного продукта")
    @Test
    void restoreDisabledProducts() {
        assertTrue(em.find(Product.class, DISABLED_PRODUCT_ID).isDisabled(), "Продукт должен быть снят с витрины");
        productService.restoreDisabledProducts(List.of(DISABLED_PRODUCT_ID));
        em.clear();
        assertFalse(em.find(Product.class, DISABLED_PRODUCT_ID).isDisabled(), "Продукт не восстановлен");
    }

    @DisplayName("Тест удаления")
    @Test
    void delete() {
        assertNotNull(em.find(Product.class, DISABLED_PRODUCT_ID), "Продукт не найден");
        productService.delete(List.of(DISABLED_PRODUCT_ID));
        assertNull(em.find(Product.class, DISABLED_PRODUCT_ID), "Продукт не удалился");

        assertThrows(ApplicationException.class, () -> productService.delete(List.of(1L)),
                "Мы должны были свалиться с ошибкой о том что, удалить можно только продукты, снятые с витрины");
    }

    @DisplayName("Тест сохранения")
    @Test
    void saveProduct() {
        var productToSave = new ProductItemFull(null, "Описание продукта", new BigDecimal(123), false, List.of(1L, 5L));
        var product = productService.saveProduct(productToSave);
        assertNotNull(product.getId(), "Продукт не сохранился");
        assertNotNull(em.find(Product.class, product.getId()), "Продукт не сохранился");
    }
}
