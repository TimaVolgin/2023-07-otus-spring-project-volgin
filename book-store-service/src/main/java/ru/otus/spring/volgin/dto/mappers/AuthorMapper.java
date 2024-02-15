package ru.otus.spring.volgin.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.spring.volgin.domain.Author;
import ru.otus.spring.volgin.dto.AuthorDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);

    List<AuthorDto> toDto(List<Author> author);

    Author fromDto(AuthorDto authorDto);
}
