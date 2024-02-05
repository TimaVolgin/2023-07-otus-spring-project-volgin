package ru.otus.spring.volgin.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.spring.volgin.domain.Book;
import ru.otus.spring.volgin.dto.BookDto;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    BookDto toDto(Book book);

    Book fromDto(BookDto bookDto);
}
