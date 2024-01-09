package ru.otus.spring.volgin.entity.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Запрос на создание пользователя
 */
@Getter
@RequiredArgsConstructor
public class CreateUserRequest {
    /** Имя пользователя */
    private final String firstName;
    /** Фамилия пользователя */
    private final String lastName;
    /** Почта пользователя */
    @Email(message = "Неверное заполнено поле email")
    @NotEmpty(message = "Необходимо указать почту")
    private final String email;
    /** Пароль пользователя */
    @NotEmpty(message = "Необходимо указать пароль")
    private final String password;
}
