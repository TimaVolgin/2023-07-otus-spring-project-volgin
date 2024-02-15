package ru.otus.spring.volgin.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

/**
 * Обработчик ошибок
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    public final ErrorInfo handleNotFoundException(ApplicationException ex, WebRequest request) {
        return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage(), request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationServiceException.class)
    public final ErrorInfo handleAuthServiceExceptionException(AuthenticationServiceException ex, WebRequest request) {
        return new ErrorInfo(HttpStatus.UNAUTHORIZED.getReasonPhrase(), ex.getMessage(), request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public final ErrorInfo handleBindException(BindException ex, WebRequest request) {
        var resultSB = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
        return new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), resultSB, request.getDescription(false));
    }
}
