package ru.otus.spring.volgin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.volgin.dto.BookDto;


/**
 * Контракт для работы с книгами
 */
public interface BookService {

    /**
     * Сохраняет книгу
     * @param book сохраняемая книга
     * @return книга
     */
    BookDto saveOrUpdate(BookDto book);

    /**
     * Возвращает книгу по идентификатору
     * @param id идентификатор книги
     * @return книга
     */
    BookDto findById(long id);

    /**
     * Возвращает все книги
     * @return список всех книг
     */
    Page<BookDto> findAll(Pageable pageable);
}
