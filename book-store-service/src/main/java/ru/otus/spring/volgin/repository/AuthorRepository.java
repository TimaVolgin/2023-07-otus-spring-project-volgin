package ru.otus.spring.volgin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.volgin.domain.Author;

/**
 * Репозиторий для работы с авторами
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
