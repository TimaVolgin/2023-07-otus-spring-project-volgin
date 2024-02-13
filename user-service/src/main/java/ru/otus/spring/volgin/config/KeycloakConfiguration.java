package ru.otus.spring.volgin.config;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Конфигурация Keycloak
 */
@Configuration
@RequiredArgsConstructor
public class KeycloakConfiguration {

    /** Свойства Keycloak */
    private final KeycloakSpringBootProperties kcProperties;

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Возвращает клиент для работы с авторизацией пользователя
     * @return клиент для работы с авторизацией пользователя
     */
    @Bean
    public AuthzClient userPasswordAuthClient() {
        var credentials = new HashMap<>(kcProperties.getCredentials());
        credentials.put("grant_type", "password");
        var conf = new org.keycloak.authorization.client.Configuration(
                kcProperties.getAuthServerUrl(), kcProperties.getRealm(),
                kcProperties.getResource(), credentials, null);
        return AuthzClient.create(conf);
    }

    /**
     * Возвращает объект для работы с API Администратора Keycloak
     * @return объект для работы с API Администратора Keycloak
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(kcProperties.getAuthServerUrl())
                .realm(kcProperties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(kcProperties.getResource())
                .clientSecret((String) kcProperties.getCredentials().get("secret"))
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }
}
