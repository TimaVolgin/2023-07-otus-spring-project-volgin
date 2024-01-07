package ru.otus.spring.volgin.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Реквизиты для получения токена
 */
@Getter
@RequiredArgsConstructor
public class CredentialsDto {
    /** Логин */
    private final String login;
    /** Пароль */
    private final String password;
}
