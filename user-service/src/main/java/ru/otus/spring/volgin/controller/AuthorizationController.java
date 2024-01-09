package ru.otus.spring.volgin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.entity.dto.CreateUserRequest;
import ru.otus.spring.volgin.entity.dto.CredentialsRequest;
import ru.otus.spring.volgin.entity.dto.TokenInfoResponse;
import ru.otus.spring.volgin.service.KeycloakAdminService;
import ru.otus.spring.volgin.service.KeycloakAuthorizationService;

import javax.validation.Valid;

/**
 * Контроллер для работы с авторизацией
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthorizationController {

    /** Сервис авторизации пользователя в Keycloak */
    public final KeycloakAuthorizationService keycloakAuthorizationService;
    /** Сервис по работе с API администратора Keycloak */
    private final KeycloakAdminService keycloakAdminService;

    /**
     * Возвращает токен
     * @param credentialsRequest реквизиты для получения токена
     * @return токен
     */
    @PostMapping("/token")
    public TokenInfoResponse getToken(@RequestBody @Valid CredentialsRequest credentialsRequest) {
        return keycloakAuthorizationService.getToken(credentialsRequest.getLogin(), credentialsRequest.getPassword());
    }

    /**
     * Возвращает токен на основе refresh-токена
     * @param refreshToken токен для получения нового токена
     * @return токен
     */
    @PostMapping("/refreshToken")
    public TokenInfoResponse refreshToken(@RequestBody String refreshToken) {
        return keycloakAuthorizationService.refreshToken(refreshToken);
    }

    /**
     * Регистрирует пользователя
     * @param createUserRequest регистрирует пользователя
     * @return токен
     */
    @PostMapping(value = "/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenInfoResponse createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return keycloakAdminService.createUser(createUserRequest);
    }
}
