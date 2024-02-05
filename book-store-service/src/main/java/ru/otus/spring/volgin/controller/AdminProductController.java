package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.dto.product.ProductFilter;
import ru.otus.spring.volgin.dto.product.ProductItem;
import ru.otus.spring.volgin.dto.product.ProductItemFull;
import ru.otus.spring.volgin.service.ProductService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для работы администратора с продуктами
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
@SecurityRequirement(name = "bearerAuth")
public class AdminProductController {

    /** Сервис для работы с продуктами */
    private final ProductService productService;

    /**
     * Возвращает продукты с заданными параметрами фильтрации
     * @param request  фильтр
     * @param pageable пагинация
     * @return продукты с заданными параметрами фильтрации
     */
    @GetMapping("/")
    public Page<ProductItem> getProducts(@Valid ProductFilter request,
                                         Pageable pageable) {
        return productService.getProducts(request, pageable);
    }

    /**
     * Возвращает продукт по идентификатору
     * @param id идентификатор продукта
     * @return продукт
     */
    @GetMapping("/{id}")
    public ProductItemFull getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * Возвращает продукты, снятые с витрины
     * @return продукты
     */
    @GetMapping("/disabled")
    public Page<ProductItem> getDisabled(Pageable pageable) {
        return productService.getDisabled(pageable);
    }

    /**
     * Снимает продукт с витрины
     */
    @PostMapping("/disable")
    public void disableProducts(@RequestBody List<Long> ids) {
        log.info("Отключение продуктов {} с витрины пользователем {}", ids, SecurityContextHolder.getContext().getAuthentication().getName());
        productService.disableProducts(ids);
    }

    /**
     * Возвращает продукты на витрину
     * @param ids идентификаторы продуктов
     */
    @PostMapping("/restore")
    public void restoreProducts(@RequestBody List<Long> ids) {
        log.info("Восстановление продуктов {} на витрину пользователем {}", ids, SecurityContextHolder.getContext().getAuthentication().getName());
        productService.restoreDisabledProducts(ids);
    }

    /**
     * Удаляет продукт полностью
     */
    @DeleteMapping("/")
    public void delete(@RequestBody List<Long> ids) {
        log.info("Удаление продуктов {} пользователем {}", ids, SecurityContextHolder.getContext().getAuthentication().getName());
        productService.delete(ids);
    }

    /**
     * Сохраняет/изменяет продукт
     * @param saveProductRequest запрос на сохранение/обновление категорий
     */
    @PostMapping("/")
    public ProductItemFull save(@RequestBody ProductItemFull saveProductRequest) {
        log.info("Создание продукта {} пользователем {}", saveProductRequest.getDescription(),
                SecurityContextHolder.getContext().getAuthentication().getName());
        return productService.saveProduct(saveProductRequest);
    }
}
