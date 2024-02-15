package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.domain.Author;
import ru.otus.spring.volgin.dto.AuthorDto;
import ru.otus.spring.volgin.dto.mappers.AuthorMapper;
import ru.otus.spring.volgin.repository.AuthorRepository;

import java.time.LocalDate;
import java.util.List;

import static java.text.MessageFormat.format;

/**
 * Сервис для работы с авторами
 */
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    /** Репозиторий для работы с авторами */
    private final AuthorRepository authorRepository;
    /** Конвертор для автора */
    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorDto> findAll() {
        return authorMapper.toDto(authorRepository.findAll());
    }

    @Override
    public AuthorDto saveOrUpdate(AuthorDto authorDto) {
        return authorMapper.toDto(authorRepository.save(authorMapper.fromDto(authorDto)));
    }

}
