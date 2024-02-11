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
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты контроллера для работы администратора с продуктами")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminProductController.class)
@Import(WebSecurityConfiguration.class)
class AdminProductControllerTest {

    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Тест получения продуктов")
    @WithMockUser(roles = "VIEWER")
    @Test
    void getProducts() throws Exception {
        var pageable = Pageable.ofSize(2);
        var filter = ProductFilter.builder().disabled(true).build();
        var products = List.of(
                new ProductItem(1L, "Продукт 1", new BigDecimal(12), true),
                new ProductItem(2L, "Продукт 2", new BigDecimal(12), true)
        );
        var page = new PageImpl<>(products, pageable, 5);
        when(productService.getProducts(refEq(filter), any())).thenReturn(page);
        mockMvc.perform(get("/admin/products/")
                        .param("disabled", String.valueOf(filter.getDisabled()))
                        .param("pageSize", String.valueOf(pageable.getPageSize()))
                        .param("pageNumber", String.valueOf(pageable.getPageNumber()))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @DisplayName("Тест получения продукта")
    @WithMockUser(roles = "VIEWER")
    @Test
    void getProduct() throws Exception {
        var productId = 2L;
        var product = new ProductItemFull(productId, "Описание продукта", new BigDecimal(123), true, null);
        when(productService.getProductById(productId)).thenReturn(product);
        mockMvc.perform(get("/admin/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }

    @DisplayName("Тест получения продуктов из корзины")
    @WithMockUser(roles = "VIEWER")
    @Test
    void getTrash() throws Exception {
        var products = List.of(new ProductItem(1L, "Описание продукта", new BigDecimal(123), true));
        var pageable = Pageable.ofSize(2);
        var page = new PageImpl<>(products, pageable, 5);
        when(productService.getDisabled(any())).thenReturn(page);
        mockMvc.perform(get("/admin/products/disabled"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @DisplayName("Тест отключения продуктов")
    @WithMockUser(roles = "EDITOR")
    @Test
    void moveToTrash() throws Exception {
        var ids = List.of(1L, 2L);
        mockMvc.perform(post("/admin/products/disable")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk());
        verify(productService).disableProducts(eq(ids));
    }

    @DisplayName("Тест восстановления из корзины")
    @WithMockUser(roles = "EDITOR")
    @Test
    void restoreTrash() throws Exception {
        var ids = List.of(1L, 2L);
        mockMvc.perform(post("/admin/products/restore")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk());
        verify(productService).restoreDisabledProducts(eq(ids));
    }

    @DisplayName("Тест удаления продуктов")
    @WithMockUser(roles = "EDITOR")
    @Test
    void testDelete() throws Exception {
        var ids = List.of(1L, 2L);
        mockMvc.perform(delete("/admin/products/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk());
        verify(productService).delete(eq(ids));
    }

    @DisplayName("Тест сохранения")
    @WithMockUser(roles = "EDITOR")
    @Test
    void save() throws Exception {
        var result = new ProductItemFull(1L, "Описание продукта", new BigDecimal(123), true, null);
        when(productService.saveProduct(refEq(result))).thenReturn(result);
        mockMvc.perform(post("/admin/products/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }
}
