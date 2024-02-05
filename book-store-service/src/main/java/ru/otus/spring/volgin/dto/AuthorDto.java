package ru.otus.spring.volgin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Автор
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    /** Идентификатор */
    private Long id;

    /** ФИО */
    private String fio;

    /** Дата рождения */
    private LocalDate birthday;
}

