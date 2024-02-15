package ru.otus.spring.volgin.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ответ на запрос, содержащий ошибку keycloak
 */
@Getter
@Setter
@NoArgsConstructor
public class KeycloakError {
    /** Сообщение об ошибке */
    private String errorMessage;
}
