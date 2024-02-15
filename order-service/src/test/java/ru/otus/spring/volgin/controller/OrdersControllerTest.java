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
import ru.otus.spring.volgin.dto.order.*;
import ru.otus.spring.volgin.dto.user.UserInfoDto;
import ru.otus.spring.volgin.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrdersController.class)
@Import(WebSecurityConfiguration.class)
class OrdersControllerTest {

    @MockBean
    private OrderService userOrderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "USER")
    @Test
    void getProduct() throws Exception {
        var order = getOrderDto();
        when(userOrderService.getOrder(2L)).thenReturn(order);
        mockMvc.perform(get("/user/orders/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }

    @Test
    void getProductUnauthorized() throws Exception {
        mockMvc.perform(get("/user/orders/{id}", 2))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "USER")
    @Test
    void getOrders() throws Exception {
        var request = Pageable.ofSize(10);
        Page<OrderShortDto> page = Page.empty(request);
        when(userOrderService.getOrders(request)).thenReturn(page);
        mockMvc.perform(get("/user/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getOrdersUnauthorized() throws Exception {
        var request = Pageable.ofSize(10);
        mockMvc.perform(get("/user/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "USER")
    @Test
    void save() throws Exception {
        var order = getOrderDto();
        var request = new UpdateOrderRequest(1L, "email", "+799", null, "address", "username");
        when(userOrderService.create(any())).thenReturn(order);
        mockMvc.perform(post("/user/orders/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }

    @Test
    void saveUnauthorized() throws Exception {
        var request = new CreateOrderRequest("email", "+799", null, "address", "username", Map.of(1L, 2));
        mockMvc.perform(post("/user/orders/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    private OrderDto getOrderDto() {
        var userInfo = new UserInfoDto("userId", "username", true, "firstName", "lastName", "email");
        var product = new OrderItemDto(3L, 4L, "title", new BigDecimal(233), 2);
        return new OrderDto(2L, LocalDateTime.now(), "email", "+799", "note",
                "address", "userId", "userName", userInfo, List.of(product));
    }
}
