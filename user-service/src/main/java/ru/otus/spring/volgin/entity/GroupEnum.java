package ru.otus.spring.volgin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupEnum {
    /** Группа администраторов */
    ADMINS("ea1cf792-cb27-4d02-855f-b2b1a2d2fb80"),
    /** Группа менеджеров с возможностью редактирования данных */
    EDITORS("769f5586-cdfb-4c66-a43b-12a98d455c76"),
    /** Группа менеджеров без возможности редактирования */
    VIEWERS("ec9af33f-101c-4c76-822f-67b6a2fe642a"),
    /** Группа пользователей */
    USERS("a2af7c21-aec9-4713-bd3f-3af2585b1260");

    /** Фиксированный идентификатор в keycloak */
    private final String groupId;
}
