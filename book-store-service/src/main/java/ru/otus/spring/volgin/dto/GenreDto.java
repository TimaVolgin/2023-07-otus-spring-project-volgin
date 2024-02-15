package ru.otus.spring.volgin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Жанр
 */
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    /** Идентификатор */
    private Long id;

    /** Название жанра */
    private String name;
}
