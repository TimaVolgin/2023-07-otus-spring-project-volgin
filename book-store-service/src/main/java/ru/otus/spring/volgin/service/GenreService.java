package ru.otus.spring.volgin.service;

import ru.otus.spring.volgin.dto.GenreDto;

import java.util.List;

/**
 * Контракт для работы с жанрами
 */
public interface GenreService {

    /**
     * Возвращает все жанры
     * @return список всех жанров
     */
    List<GenreDto> findAll();

    /**
     * Сохраняет жанр
     * @param genreDto жанр
     * @return жанр
     */
    GenreDto saveOrUpdate(GenreDto genreDto);

}
