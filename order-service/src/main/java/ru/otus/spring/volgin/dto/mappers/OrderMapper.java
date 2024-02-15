package ru.otus.spring.volgin.dto.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.volgin.domain.Order;
import ru.otus.spring.volgin.domain.OrderItem;
import ru.otus.spring.volgin.dto.order.*;
import ru.otus.spring.volgin.dto.user.UserInfoDto;
import ru.otus.spring.volgin.feign.BookStoreServiceClient;
import ru.otus.spring.volgin.feign.UserServiceClient;

import java.util.List;
import java.util.Map;

/**
 * Конвертер заказов
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class OrderMapper {

    @Autowired
    private BookStoreServiceClient bookStoreServiceClient;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private ProductToOrderMapper productToOrderMapper;

    /**
     * Конвертирует заказ в dto
     * @param order заказ
     * @return заказ dto
     */
    @Mapping(target = "userInfo", source = "order", qualifiedByName = "setUserInfo")
    public abstract OrderDto orderToDtoWithUserInfo(Order order);

    /**
     * Конвертирует заказ в dto
     * @param order заказ
     * @return заказ dto
     */
    public abstract OrderDto orderToDto(Order order);

    /**
     * Конвертирует товар заказа в dto
     * @param order заказ
     * @return товар заказа dto
     */
    public abstract OrderItemDto orderToDto(OrderItem order);

    /**
     * Обновляет сущность на основе DTO
     * @param source     dto
     * @param order    обновляемая сущность
     */
    @Mapping(target = "items", ignore = true)
    public abstract void updateOrderFromDto(UpdateOrderRequest source, @MappingTarget Order order);

    /**
     * Создает объект заказа на основе запроса
     * @param request запрос
     * @param userId  идентификатор пользователя
     * @return объект заказа
     */
    @Mapping(target = "items", source = "request.productIds", qualifiedByName = "orderItemsFromProductIds")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    public abstract Order createRequestToOrder(CreateOrderRequest request, String userId);

    /**
     * Формирует продукты заказа на основе идентификаторов
     * @param productMap карта идентификаторов продуктов и количества в заказе
     * @return продукты заказа на основе идентификаторов
     */
    @Named("orderItemsFromProductIds")
    List<OrderItem> orderItemsFromProductIds(Map<Long, Integer> productMap) {
        return bookStoreServiceClient.getProductsByIds(productMap.keySet()).stream()
                .map(product -> productToOrderMapper.map(product, productMap.get(product.getId())))
                .toList();
    }

    /**
     * Устанавливает обратную связь заказов и продуктов
     * @param order заказ
     */
    @AfterMapping
    void afterMapping(@MappingTarget Order order) {
        order.getItems().forEach(item ->  item.setOrder(order));
    }

    @Named("setUserInfo")
    UserInfoDto setUserInfo(Order order) {
        return userServiceClient.getUserInfo(order.getUserId());
    }
}
