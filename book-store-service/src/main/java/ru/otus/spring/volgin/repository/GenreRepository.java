package ru.otus.spring.volgin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.volgin.domain.Genre;

/**
 * Репозиторий для работы с жанрами
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
