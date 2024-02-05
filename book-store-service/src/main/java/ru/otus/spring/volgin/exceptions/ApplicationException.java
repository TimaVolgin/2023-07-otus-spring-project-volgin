package ru.otus.spring.volgin.exceptions;

import static java.lang.String.format;

/**
 * Ошибка текст которой транслируется наружу
 */
public class ApplicationException extends RuntimeException {

    /**
     * Конструктор
     * @param message текс сообщения
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Конструктор
     * @param message текс сообщения
     * @param ex      родительское исключение
     */
    public ApplicationException(String message, Exception ex) {
        super(message, ex);
    }

    /**
     * Конструктор
     * @param messagePattern шаблон текста сообщения (см {@link String#format})
     * @param args           подставляемые аргументы для шаблона
     */
    public ApplicationException(String messagePattern, Object... args) {
        super(format(messagePattern, args));
    }
}
