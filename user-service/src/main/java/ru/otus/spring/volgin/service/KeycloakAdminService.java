package ru.otus.spring.volgin.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.springdoc.core.converters.models.Pageable;

import java.util.List;

/**
 * Интерфейс по работе с API Keycloak
 */
public interface KeycloakAdminService {

    /**
     * Возвращает список пользователей
     *
     */
    List<UserRepresentation> getUsers(Pageable pageable);

}
