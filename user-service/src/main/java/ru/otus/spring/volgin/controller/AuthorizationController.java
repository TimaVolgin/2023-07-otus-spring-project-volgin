package ru.otus.spring.volgin.controller;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.dto.CredentialsDto;
import ru.otus.spring.volgin.service.KeycloakAuthorizationService;

/**
 * Контроллер для работы с авторизацией
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    /** Сервис авторизации пользователя в Keycloak */
    public final KeycloakAuthorizationService keycloakAuthorizationService;

    /**
     * Возвращает токен
     * @param credentialsDto реквизиты для получения токена
     * @return токен
     */
    @GetMapping("/token")
    public AccessTokenResponse getToken(CredentialsDto credentialsDto) {
        return keycloakAuthorizationService.getToken(credentialsDto);
    }

    /**
     * Возвращает токен на основе refresh-токена
     * @param refreshToken токен для получения нового токена
     * @return токен
     */
    @GetMapping("/refresh")
    public AccessTokenResponse refreshToken(String refreshToken) {
        return keycloakAuthorizationService.refreshToken(refreshToken);
    }
}
