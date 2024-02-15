package ru.otus.spring.volgin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Книга
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    /** Идентификатор */
    private Long id;

    /** Название книги */
    @NotBlank(message = "Необходимо указать название книги")
    @Size(min = 5, message = "Минимальное количество символов: 5")
    private String title;

    /** Дата публикации книги */
    @NotNull(message = "Необходимо указать дату публикации")
    private LocalDate published;

    /** Количество книг в наличии */
    @NotNull(message = "Необходимо указать количество книг")
    private Long numberOf;

    /** Жанр */
    @Valid
    private GenreDto genre;

    /** Автор */
    @Valid
    private AuthorDto author;
}
