package ru.otus.spring.volgin.service;

import ru.otus.spring.volgin.entity.dto.TokenInfoResponse;

/**
 * Интерфейс авторизации пользователя в Keycloak
 */
public interface KeycloakAuthorizationService {

    /**
     * Возвращает токен
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return токен
     */
    TokenInfoResponse getToken(String login, String password);

    /**
     * Возвращает токен на основе refresh Токена
     * @param refreshToken токен для получения нового токена
     * @return токен
     */
    TokenInfoResponse refreshToken(String refreshToken);
}
