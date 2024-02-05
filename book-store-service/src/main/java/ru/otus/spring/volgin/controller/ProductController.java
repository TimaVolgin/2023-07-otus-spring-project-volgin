package ru.otus.spring.volgin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.dto.product.ProductFilter;
import ru.otus.spring.volgin.dto.product.ProductItem;
import ru.otus.spring.volgin.dto.product.ProductItemFull;
import ru.otus.spring.volgin.service.ProductService;

import java.util.List;

/**
 * Контроллер для работы клиента с продуктами
 */
@RestController
@RequestMapping("/user/products")
@RequiredArgsConstructor
public class ProductController {

    /** Сервис для работы с продуктами */
    private final ProductService productService;

    /**
     * Возвращает продукты с витрины, отфильтрованные по идентификатору книги
     * @param bookId   идентификатор книги
     * @param pageable пагинация
     * @return отфильтрованные по идентификатору книги продукты с витрины
     */
    @GetMapping("/")
    public Page<ProductItem> getProducts(@RequestParam(name = "bookId") Long bookId,
                                         Pageable pageable) {
        var request = ProductFilter.builder()
                .disabled(false)
                .bookId(bookId)
                .build();
        return productService.getProducts(request, pageable);
    }

    @GetMapping("/{id}")
    public ProductItemFull getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/list")
    public List<ProductItem> getProductsByIds(@RequestParam(name = "ids") List<Long> ids) {
        return productService.getActiveProductsByIds(ids);
    }
}
