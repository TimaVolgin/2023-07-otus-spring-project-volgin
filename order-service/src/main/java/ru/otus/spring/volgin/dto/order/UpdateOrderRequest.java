package ru.otus.spring.volgin.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Запрос на обновление заказа
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UpdateOrderRequest {

    /** Идентификатор */
    @NotNull(message = "Не задан идентификатор")
    private Long id;
    /** Email указанный при заказе */
    private String email;
    /** Телефон указанный для связи */
    @NotEmpty(message = "Требуется указать телефон")
    private String phone;
    /** Заметка */
    private String note;
    /** Адрес */
    @NotEmpty(message = "Требуется указать адрес")
    private String address;
    /** Имя контактного лица */
    @NotEmpty(message = "Требуется указать имя пользователя")
    private String username;
}
