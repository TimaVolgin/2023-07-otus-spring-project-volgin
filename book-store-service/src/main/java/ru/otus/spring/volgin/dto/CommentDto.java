package ru.otus.spring.volgin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Комментарий для книги
 */
@Getter
@ToString
@AllArgsConstructor
public class CommentDto {

    /** Идентификатор */
    private Long id;

    /** Комментарий */
    private String text;
}
