package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.entity.GroupEnum;
import ru.otus.spring.volgin.entity.dto.CreateUserRequest;
import ru.otus.spring.volgin.entity.dto.KeycloakError;
import ru.otus.spring.volgin.entity.dto.TokenInfoResponse;
import ru.otus.spring.volgin.exceptions.ApplicationException;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Сервис по работе с API Keycloak
 */
@Service
@RequiredArgsConstructor
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    /** Объект по работе с админским API keycloak */
    private final Keycloak keycloak;
    /** Свойства keycloak */
    private final KeycloakSpringBootProperties kcProperties;
    /** Сервис по работе с авторизацией */
    private final KeycloakAuthorizationService keycloakAuthorizationService;

    @Override
    public TokenInfoResponse createUser(CreateUserRequest createUserRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(createUserRequest.getEmail());
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setEmail(createUserRequest.getEmail());

        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(createUserRequest.getPassword());
        user.setCredentials(List.of(passwordCredentials));

        UsersResource usersResource = keycloak.realm(kcProperties.getRealm()).users();

        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            throw new ApplicationException("Не удалось создать пользователя: " + response.readEntity(KeycloakError.class).getErrorMessage());
        }
        return keycloakAuthorizationService.getToken(createUserRequest.getEmail(), createUserRequest.getPassword());
    }

    @Override
    public List<UserRepresentation> getUsers(Pageable pageable) {
        return keycloak.realm(kcProperties.getRealm())
                .users()
                .list(pageable.getPage() * pageable.getSize(), pageable.getSize());
    }

    @Override
    public void setUserGroup(String userId, GroupEnum group) {
        try {
            UsersResource usersResource = keycloak.realm(kcProperties.getRealm()).users();
            var existGroups = usersResource.get(userId).groups();
            existGroups.forEach(g -> usersResource.get(userId)
                    .leaveGroup(g.getId()));
            usersResource.get(userId).joinGroup(group.getGroupId());
        } catch (Exception e) {
            throw new ApplicationException("Не удалось изменить группу пользователя. Попробуйте ещё раз или обратитесь к администратору", e);
        }
    }
}
