package ru.otus.spring.volgin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.volgin.domain.Book;
import ru.otus.spring.volgin.dto.AuthorDto;
import ru.otus.spring.volgin.dto.BookDto;
import ru.otus.spring.volgin.dto.GenreDto;
import ru.otus.spring.volgin.dto.mappers.AuthorMapperImpl;
import ru.otus.spring.volgin.dto.mappers.BookMapperImpl;
import ru.otus.spring.volgin.dto.mappers.GenreMapperImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Интеграционный тест сервиса для работы с книгами")
@DataJpaTest
@Import({BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
class BookServiceImplTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Тест сохранения")
    @Test
    void saveBook() {
        var bookToSave = new BookDto(2L, "Название книги", LocalDate.now(), 10L,
                new GenreDto(1L, "Жанр"), new AuthorDto(2L, "Автор", LocalDate.now()));
        var book = bookService.saveOrUpdate(bookToSave);
        assertNotNull(book.getId(), "Книга не сохранена");
        assertNotNull(em.find(Book.class, book.getId()), "Книга не сохранена");
    }

    @DisplayName("Тест получения книги по идентификатору")
    @Test
    void getBookById() {
        var book = bookService.findById(3L);
        assertEquals(3L, book.getId());
        assertEquals("Евгений Онегин", book.getTitle());
        assertEquals(0L, book.getNumberOf());
        assertEquals("Александр Сергеевич Пушкин", book.getAuthor().getFio());
    }

    @DisplayName("Тест получения всех книг")
    @Test
    void findAllBooks() {
        var pageable = Pageable.ofSize(2);
        var books = bookService.findAll(pageable);
        assertEquals(2, books.getSize());
        assertEquals(7, books.getTotalElements());
    }
}
