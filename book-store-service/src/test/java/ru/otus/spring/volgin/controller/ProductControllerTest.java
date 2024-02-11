package ru.otus.spring.volgin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.volgin.config.WebSecurityConfiguration;
import ru.otus.spring.volgin.dto.product.ProductFilter;
import ru.otus.spring.volgin.dto.product.ProductItem;
import ru.otus.spring.volgin.dto.product.ProductItemFull;
import ru.otus.spring.volgin.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты контроллера для работы клиента с продуктами")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
@Import(WebSecurityConfiguration.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Тест получения продуктов")
    @Test
    void getProducts() throws Exception {
        var bookId = 5L;
        var pageable = Pageable.ofSize(2);
        var filter = ProductFilter.builder().disabled(false).bookId(bookId).build();
        var products = List.of(
                new ProductItem(1L, "Продукт 1", new BigDecimal(12), true),
                new ProductItem(2L, "Продукт 2", new BigDecimal(12), false)
        );
        var page = new PageImpl<>(products, pageable, 5);
        when(productService.getProducts(refEq(filter), any())).thenReturn(page);
        mockMvc.perform(get("/user/products/")
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
        verify(productService).getProducts(refEq(filter), any());
    }

    @DisplayName("Тест получения продукта")
    @Test
    void getProduct() throws Exception {
        var productId = 2L;
        var product = new ProductItemFull(productId, "Описание продукта", new BigDecimal(123), true, null);
        when(productService.getProductById(productId)).thenReturn(product);
        mockMvc.perform(get("/user/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }

    @DisplayName("Тест получения продуктов по идентификаторам")
    @Test
    void testGetProductsByIds() throws Exception {
        var ids = List.of(1L, 2L);
        var products = List.of(
                new ProductItem(1L, "Продукт 1", new BigDecimal(12), true),
                new ProductItem(2L, "Продукт 2", new BigDecimal(12), false)
        );
        when(productService.getActiveProductsByIds(eq(ids))).thenReturn(products);
        mockMvc.perform(get("/user/products/list")
                        .param("ids", "1")
                        .param("ids", "2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(products)));
    }
}
