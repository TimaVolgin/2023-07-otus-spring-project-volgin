package ru.otus.spring.volgin.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Заказ в укороченном виде
 */
@AllArgsConstructor
@Setter
@Getter
public class OrderShortDto {

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
    /** Цена заказа */
    private BigDecimal totalPrice;
}
