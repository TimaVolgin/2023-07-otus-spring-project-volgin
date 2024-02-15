package ru.otus.spring.volgin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.volgin.config.WebSecurityConfiguration;
import ru.otus.spring.volgin.dto.order.OrderDto;
import ru.otus.spring.volgin.dto.order.OrderItemDto;
import ru.otus.spring.volgin.dto.order.OrderShortDto;
import ru.otus.spring.volgin.dto.order.UpdateOrderRequest;
import ru.otus.spring.volgin.dto.user.UserInfoDto;
import ru.otus.spring.volgin.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminOrdersController.class)
@Import(WebSecurityConfiguration.class)
class AdminOrdersControllerTest {

    @MockBean
    private OrderService adminOrderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void getProduct() throws Exception {
        var order = getOrderDto();
        when(adminOrderService.getOrder(2L)).thenReturn(order);
        mockMvc.perform(get("/admin/orders/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }

    @Test
    void getProductUnauthorized() throws Exception {
        mockMvc.perform(get("/admin/orders/{id}", 2))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "USER")
    @Test
    void getProductForbidden() throws Exception {
        mockMvc.perform(get("/admin/orders/{id}", 2))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getOrders() throws Exception {
        var request = Pageable.ofSize(10);
        Page<OrderShortDto> page = Page.empty(request);
        when(adminOrderService.getOrders(request)).thenReturn(page);
        mockMvc.perform(get("/admin/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    void getOrdersForbidden() throws Exception {
        var request = Pageable.ofSize(10);
        mockMvc.perform(get("/admin/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void update() throws Exception {
        var order = getOrderDto();
        var request = new UpdateOrderRequest(1L, "email", "+799", null, "address", "username");
        when(adminOrderService.update(any())).thenReturn(order);
        mockMvc.perform(post("/admin/orders/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }

    @WithMockUser(roles = "VIEWER")
    @Test
    void updateForbidden() throws Exception {
        var request = new UpdateOrderRequest(1L, "email", "+799", null, "address", "username");
        mockMvc.perform(post("/admin/orders/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    private OrderDto getOrderDto() {
        var userInfo = new UserInfoDto("userId", "username", true, "firstName", "lastName", "email");
        var product = new OrderItemDto(3L, 4L, "title", new BigDecimal(233), 2);
        return new OrderDto(2L, LocalDateTime.now(), "email", "+799", "note",
                "address", "userId", "userName", userInfo, List.of(product));
    }
}
