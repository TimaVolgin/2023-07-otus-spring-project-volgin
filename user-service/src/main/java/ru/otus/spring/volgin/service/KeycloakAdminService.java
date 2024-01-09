package ru.otus.spring.volgin.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.springdoc.core.converters.models.Pageable;
import ru.otus.spring.volgin.entity.GroupEnum;
import ru.otus.spring.volgin.entity.dto.CreateUserRequest;
import ru.otus.spring.volgin.entity.dto.TokenInfoResponse;

import java.util.List;

/**
 * Интерфейс по работе с API администратора Keycloak
 */
public interface KeycloakAdminService {

    /**
     * Возвращает список пользователей
     * @param createUserRequest запрос на создание пользователя
     * @return ответ на запрос, содержащий информацию о токене
     */
    TokenInfoResponse createUser(CreateUserRequest createUserRequest);

    /**
     * Возвращает список пользователей
     */
    List<UserRepresentation> getUsers(Pageable pageable);

    /**
     * Устанавливает группу пользователя
     * @param userId идентификатор пользователя
     * @param group группа пользователей
     */
    void    setUserGroup(String userId, GroupEnum group);

}
