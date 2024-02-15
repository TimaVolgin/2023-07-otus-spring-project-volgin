package ru.otus.spring.volgin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.volgin.dto.product.ProductItem;

import java.util.List;
import java.util.Set;

/**
 * Клиент для работы с продуктовым сервисом
 */
@FeignClient("book-store-service")
public interface BookStoreServiceClient {

    /**
     * Возвращает список продуктов на основе идентификаторов
     * @param ids идентификаторы
     * @return список продуктов
     */
    @GetMapping("/user/products/list")
    List<ProductItem> getProductsByIds(@RequestParam(name = "ids") Set<Long> ids);
}
