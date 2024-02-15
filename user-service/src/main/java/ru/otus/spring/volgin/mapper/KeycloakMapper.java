package ru.otus.spring.volgin.mapper;

import org.keycloak.representations.AccessTokenResponse;
import org.mapstruct.Mapper;
import ru.otus.spring.volgin.entity.dto.TokenInfoResponse;

/** Конвертер keycloak объектов в DTO */
@Mapper(componentModel = "spring")
public interface KeycloakMapper {

    TokenInfoResponse tokenToDto(AccessTokenResponse tokenResponse);
}
