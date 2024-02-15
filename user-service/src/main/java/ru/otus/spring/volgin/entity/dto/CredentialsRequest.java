package ru.otus.spring.volgin.entity.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Запрос с реквизитами для получения токена
 */
@Getter
@RequiredArgsConstructor
public class CredentialsRequest {
    /** Логин */
    @NotEmpty(message = "Необходимо ввести логин")
    private final String login;
    /** Пароль */
    @NotEmpty(message = "Необходимо ввести пароль")
    private final String password;
}
