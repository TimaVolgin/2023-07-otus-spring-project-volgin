package ru.otus.spring.volgin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.otus.spring.volgin.domain.Author;
import ru.otus.spring.volgin.domain.Book;
import ru.otus.spring.volgin.domain.Genre;
import ru.otus.spring.volgin.dto.BookDto;
import ru.otus.spring.volgin.dto.mappers.BookMapper;
import ru.otus.spring.volgin.repository.AuthorRepository;
import ru.otus.spring.volgin.repository.BookRepository;
import ru.otus.spring.volgin.repository.GenreRepository;

import static java.text.MessageFormat.format;

/**
 * Сервис для работы с книгами
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    /** Репозиторий для работы с книгами */
    private final BookRepository bookRepository;
    /** Репозиторий для работы с жанрами */
    private final GenreRepository genreRepository;
    /** Репозиторий для работы с авторами */
    private final AuthorRepository authorRepository;
    /** Конвертер для книги */
    private final BookMapper bookMapper;

    @Override
    public BookDto saveOrUpdate(BookDto bookDto) {
        Genre genre = getGenre(bookDto.getGenre().getId());
        Author author = getAuthor(bookDto.getAuthor().getId());
        Book book = bookMapper.fromDto(bookDto);
        book.setGenre(genre);
        book.setAuthor(author);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto findById(long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найдена книга с идентификатором {0}", id))));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    private Genre getGenre(long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден жанр с именем {0}", genreId)));
    }

    private Author getAuthor(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(format("Не найден автор с идентификатором {0}", authorId)));
    }
}
