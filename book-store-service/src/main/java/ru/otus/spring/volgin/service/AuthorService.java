package ru.otus.spring.volgin.service;

import ru.otus.spring.volgin.dto.AuthorDto;

import java.util.List;

/**
 * Контракт для работы с авторами
 */
public interface AuthorService {
    /**
     * Возвращает всех авторов
     * @return список всех авторов
     */
    List<AuthorDto> findAll();

    /**
     * Сохраняет автора
     * @param authorDto автор
     * @return автор
     */
    AuthorDto saveOrUpdate(AuthorDto authorDto);

}
