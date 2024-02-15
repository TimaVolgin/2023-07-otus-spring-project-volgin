package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.entity.dto.TokenInfoResponse;
import ru.otus.spring.volgin.mapper.KeycloakMapper;

/**
 * Сервис авторизации пользователя в Keycloak
 */
@Service
@RequiredArgsConstructor
public class KeycloakAuthorizationServiceImpl implements KeycloakAuthorizationService {

    /** Клиент для работы с авторизацией keycloak */
    private final AuthzClient authzClient;
    /** Свойства keycloak */
    private final KeycloakSpringBootProperties kcProperties;
    /** Конвертер keycloak объектов в DTO */
    private final KeycloakMapper keycloakMapper;

    @Override
    public TokenInfoResponse getToken(String login, String password) {
        try {
            return keycloakMapper.tokenToDto(authzClient.authorization(login, password)
                    .authorize());
        } catch (Exception e) {
            throw new AuthenticationServiceException("Не удалось авторизоваться, попробуйте ещё раз", e);
        }
    }

    @Override
    public TokenInfoResponse refreshToken(String refreshToken) {
        String url = kcProperties.getAuthServerUrl() + "/realms/" + kcProperties.getRealm() + "/protocol/openid-connect/token";
        String clientId = kcProperties.getResource();
        String secret = (String) kcProperties.getCredentials().get("secret");
        Http http = new Http(authzClient.getConfiguration(), (params, headers) -> {});

        var tokenResponse =  http.<AccessTokenResponse>post(url)
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
        return keycloakMapper.tokenToDto(tokenResponse);
    }
}
