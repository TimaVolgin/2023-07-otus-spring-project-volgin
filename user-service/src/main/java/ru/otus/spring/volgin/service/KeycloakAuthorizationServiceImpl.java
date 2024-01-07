package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.dto.CredentialsDto;

/**
 * Сервис авторизации пользователя в Keycloak
 */
@Service
@RequiredArgsConstructor
public class KeycloakAuthorizationServiceImpl implements KeycloakAuthorizationService {

    /** Клиент для работы с авторизацией keycloak */
    private final AuthzClient authzClient;
    /** Конфигурация keycloak */
    private final Configuration kcConfig;
    /** Свойства keycloak */
    private final KeycloakSpringBootProperties kcProperties;

    @Override
    public AccessTokenResponse getToken(CredentialsDto credentialsDto) {
        return authzClient.authorization(credentialsDto.getLogin(), credentialsDto.getPassword())
                .authorize();
    }

    @Override
    public AccessTokenResponse refreshToken(String refreshToken) {
        String url = kcProperties.getAuthServerUrl() + "/realms/" + kcProperties.getRealm() + "/protocol/openid-connect/token";
        String clientId = kcProperties.getResource();
        String secret = (String) kcProperties.getCredentials().get("secret");
        Http http = new Http(kcConfig, (params, headers) -> {});

        return http.<AccessTokenResponse>post(url)
                .authentication()
                .client()
                .form()
                .param("grant_type", "refresh_token")
                .param("refresh_token", refreshToken)
                .param("client_id", clientId)
                .param("client_secret", secret)
                .response()
                .json(AccessTokenResponse.class)
                .execute();
    }
}
