package ru.otus.spring.volgin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.volgin.domain.Book;

import java.util.Optional;

/**
 * Репозиторий для работы с книгами
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Возвращает книгу по идентификатору
     * @param id идентификатор книги
     * @return книга
     */
    @Override
    @EntityGraph(value = "book")
    Optional<Book> findById(Long id);
}
