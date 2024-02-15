package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.entity.GroupEnum;
import ru.otus.spring.volgin.service.KeycloakAdminService;

import java.util.List;

/**
 * Контроллер для работы с администратором
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    /** Сервис по работе с API Keycloak */
    private final KeycloakAdminService keycloakAdminService;

    /**
     * Возвращает пользователя по идентификатору
     * @param id идентификатор
     * @return пользователь
     */
    @GetMapping("/users/{id}")
    public UserRepresentation getUser(@PathVariable String id) {
        return keycloakAdminService.getUser(id);
    }

    /**
     * Возвращает список пользователей
     * @param pageable пагинация страницы
     * @return список пользователей
     */
    @GetMapping("/users")
    public List<UserRepresentation> getUsers(Pageable pageable) {
        return keycloakAdminService.getUsers(pageable);
    }

    /**
     * Возвращает список групп
     * @return список групп
     */
    @GetMapping("/groups")
    public List<GroupRepresentation> getGroups() {
        return keycloakAdminService.getGroups();
    }

    /**
     * Устанавливает группу пользователя
     * @param userId идентификатор пользователя
     * @param group группа пользователей
     */
    @PostMapping("/users/{userId}/group")
    public void setUserGroup(@PathVariable("userId") String userId, @RequestBody GroupEnum group) {
        log.info("Установка группы {} для пользователя {} пользователем {}", group, userId,
                SecurityContextHolder.getContext().getAuthentication().getName());
        keycloakAdminService.setUserGroup(userId, group);
    }

}
