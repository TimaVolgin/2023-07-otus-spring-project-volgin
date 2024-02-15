package ru.otus.spring.volgin.dto.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import ru.otus.spring.volgin.order_service.WireMockConfig;
import ru.otus.spring.volgin.domain.Order;
import ru.otus.spring.volgin.domain.OrderItem;
import ru.otus.spring.volgin.dto.order.CreateOrderRequest;
import ru.otus.spring.volgin.dto.order.UpdateOrderRequest;
import ru.otus.spring.volgin.dto.product.ProductItem;
import ru.otus.spring.volgin.dto.user.UserInfoDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("Тест маппера с интеграцией с другими сервисами")
@Import(WireMockConfig.class)
class OrderMapperTest {

    private static final String USER_ID = "userId";

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WireMockServer mockBookStoreService;
    @Autowired
    private WireMockServer mockUserService;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Тестирует маппинг заказа в dto")
    @Test
    void orderToDto() {
        var order = getOrder();
        var dto = orderMapper.orderToDto(order);
        assertThat(order)
                .usingRecursiveComparison()
                .ignoringFields("items.order")
                .isEqualTo(dto);
    }

    @DisplayName("Тестирует маппинг заказа в dto, обогащая информацией о пользователях из другого сервиса")
    @Test
    void orderToDtoWithUserInfo() throws JsonProcessingException {
        var userInfo = mockUserService();
        var order = getOrder();
        var dto = orderMapper.orderToDtoWithUserInfo(order);
        assertThat(order)
                .usingRecursiveComparison()
                .ignoringFields("items.order", "userInfo")
                .isEqualTo(dto);
        assertThat(userInfo)
                .usingRecursiveComparison()
                .isEqualTo(dto.getUserInfo());
    }

    @DisplayName("Тестирует модификацию заказа на основе dto")
    @Test
    void updateOrderFromDto() {
        var updatedOrder = getOrder();
        var request = new UpdateOrderRequest();
        request.setPhone("123");
        request.setAddress("address");
        request.setEmail("email");
        assertThat(request)
                .usingRecursiveComparison()
                .ignoringFields("items")
                .isNotEqualTo(updatedOrder);
        orderMapper.updateOrderFromDto(request, updatedOrder);
        assertThat(request)
                .usingRecursiveComparison()
                .ignoringFields("items")
                .isEqualTo(updatedOrder);
    }

    @DisplayName("Тестирует создание заказа, обогащая информацией о продукте из сервиса продуктов")
    @Test
    void createRequestToOrder() throws JsonProcessingException {
        mockBookStoreProducts();
        var request = new CreateOrderRequest("email", "+7911111111", "until 12 p.m.", "address", "username", Map.of(1L, 2, 2L, 3));
        var order = orderMapper.createRequestToOrder(request, "user-id");
        assertThat(order)
                .usingRecursiveComparison()
                .ignoringFields("id", "created", "items", "userId")
                .isEqualTo(request);
        assertEquals("user-id", order.getUserId());
        assertNotNull(order.getCreated());
        assertEquals(2, order.getItems().size());
        assertEquals(order, order.getItems().get(0).getOrder());
        assertEquals(2, order.getItems().get(0).getCount());
        assertEquals(order, order.getItems().get(1).getOrder());
        assertEquals(3, order.getItems().get(1).getCount());
    }

    private List<ProductItem> mockBookStoreProducts() throws JsonProcessingException {
        var products = List.of(ProductItem.builder()
                        .id(1L)
                        .description("Продукт 1")
                        .price(new BigDecimal(100))
                        .disabled(false)
                        .build(),
                ProductItem.builder()
                        .id(2L)
                        .description("Продукт 2")
                        .price(new BigDecimal(400))
                        .disabled(false)
                        .build()
        );
        mockBookStoreService.stubFor(WireMock.get(urlMatching("/user/products/list\\?(.*)"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(products))));
        return products;
    }

    private UserInfoDto mockUserService() throws JsonProcessingException {
        var userInfo = UserInfoDto.builder()
                .username("")
                .email("petrovi@mail.ru")
                .id(USER_ID)
                .lastName("Petrov")
                .firstName("Ivan")
                .build();
        mockUserService.stubFor(WireMock.get(urlMatching("/admin/users/" + USER_ID))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(userInfo))));
        return userInfo;
    }

    private Order getOrder() {
        var order = new Order();
        order.setId(1L);
        order.setAddress("address");
        order.setEmail("email");
        order.setPhone("phone");
        order.setUsername("username");
        order.setUserId(USER_ID);
        order.setCreated(LocalDateTime.now());
        order.setItems(List.of(OrderItem.builder()
                .id(1L)
                .productId(4L)
                .description("Продукт 1")
                .price(new BigDecimal(100))
                .count(2)
                .build()));
        return order;
    }
}
