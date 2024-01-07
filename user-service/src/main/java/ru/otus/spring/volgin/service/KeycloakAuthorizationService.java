package ru.otus.spring.volgin.service;

import org.keycloak.representations.AccessTokenResponse;
import ru.otus.spring.volgin.dto.CredentialsDto;

/**
 * Интерфейс авторизации пользователя в Keycloak
 */
public interface KeycloakAuthorizationService {

    /**
     * Возвращает токен
     * @param credentialsDto реквизиты пользователя
     * @return токен
     */
    AccessTokenResponse getToken(CredentialsDto credentialsDto);

    /**
     * Возвращает токен на основе refresh Токена
     * @param refreshToken токен для получения нового токена
     * @return токен
     */
    AccessTokenResponse refreshToken(String refreshToken);
}
