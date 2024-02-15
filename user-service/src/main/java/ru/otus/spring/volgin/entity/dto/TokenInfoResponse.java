package ru.otus.spring.volgin.entity.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Ответ на запрос, содержащий информацию о токене
 */
@Getter
@RequiredArgsConstructor
public class TokenInfoResponse {
    /** Токен */
    private final String token;
    /** Токен для обновления токена (refresh-токен) */
    private final String refreshToken;
    /** Тип токена */
    private final String tokenType;
    /** Время жизни токена */
    private final long expiresIn;
    /** Время жизни токена для обновления токена */
    private final long refreshExpiresIn;

}
