package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.domain.Genre;
import ru.otus.spring.volgin.dto.GenreDto;
import ru.otus.spring.volgin.dto.mappers.GenreMapper;
import ru.otus.spring.volgin.repository.GenreRepository;

import java.util.List;

import static java.text.MessageFormat.format;

/**
 * Сервис для работы с жанрами
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    /** Репозиторий для работы с жанрами */
    private final GenreRepository genreRepository;
    /** Конвертер для жанра */
    private final GenreMapper genreMapper;

    @Override
    public List<GenreDto> findAll() {
        return genreMapper.toDto(genreRepository.findAll());
    }

    @Override
    public GenreDto saveOrUpdate(GenreDto genreDto) {
        return genreMapper.toDto(genreRepository.save(genreMapper.fromDto(genreDto)));
    }
}
