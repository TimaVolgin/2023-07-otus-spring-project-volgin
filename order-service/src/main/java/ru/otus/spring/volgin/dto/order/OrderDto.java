package ru.otus.spring.volgin.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.volgin.dto.user.UserInfoDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Заказ
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderDto {

    /** Идентификатор */
    private Long id;
    /** Время создания */
    private LocalDateTime created;
    /** Email указанный при заказе */
    private String email;
    /** Телефон указанный для связи */
    private String phone;
    /** Заметка */
    private String note;
    /** Адрес */
    private String address;
    /** Идентификатор пользователя из keycloak */
    private String userId;
    /** Имя контактного лица */
    private String username;
    /** Подробная информация о пользователе */
    private UserInfoDto userInfo;
    /** Продукты в заказе */
    private List<OrderItemDto> items;
}
