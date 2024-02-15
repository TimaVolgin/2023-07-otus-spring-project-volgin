package ru.otus.spring.volgin.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Информация об ошибке API
 */
@Getter
@RequiredArgsConstructor
public class ErrorInfo {

    /** Статус */
    private final String status;
    /** Сообщение */
    private final String message;
    /** Детали */
    private final String details;
}
