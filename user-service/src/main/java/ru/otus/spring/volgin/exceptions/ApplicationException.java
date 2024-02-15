package ru.otus.spring.volgin.exceptions;

/**
 * Ошибка приложения
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Exception ex) {
        super(message, ex);
    }
}
