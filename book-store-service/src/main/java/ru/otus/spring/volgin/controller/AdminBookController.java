package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.dto.BookDto;
import ru.otus.spring.volgin.service.BookService;

import javax.validation.Valid;

/**
 * Контроллер работы с книгами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/books")
@SecurityRequirement(name = "bearerAuth")
public class AdminBookController {

    /** Сервис работы с книгами */
    private final BookService bookService;

    /**
     * Возвращает список книг
     * @param pageable пагинация и сортировка
     * @return список книг
     */
    @GetMapping("/")
    public Page<BookDto> getBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    /**
     * Возвращает книгу
     * @param bookId идентификатор книги
     * @return книга
     */
    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable Long bookId) {
        return bookService.findById(bookId);
    }

    /**
     * Сохраняет или обновляет книгу
     * @param bookDto книга
     * @return книга
     */
    @PostMapping("/")
    public BookDto saveOrUpdate(@Valid @RequestBody BookDto bookDto) {
        return bookService.saveOrUpdate(bookDto);
    }
}
