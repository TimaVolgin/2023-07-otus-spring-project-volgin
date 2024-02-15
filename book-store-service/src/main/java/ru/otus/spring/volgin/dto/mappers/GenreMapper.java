package ru.otus.spring.volgin.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.spring.volgin.domain.Genre;
import ru.otus.spring.volgin.dto.GenreDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toDto(Genre genre);
    List<GenreDto> toDto(List<Genre> genres);
    Genre fromDto(GenreDto genreDto);
}
