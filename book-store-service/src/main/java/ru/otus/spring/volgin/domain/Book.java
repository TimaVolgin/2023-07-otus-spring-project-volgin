package ru.otus.spring.volgin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Книга
 */
@Builder
@Getter
@Setter
@ToString(exclude = "products")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book", includeAllAttributes = true)
public class Book {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название книги */
    @Column(name = "title", nullable = false)
    private String title;

    /** Дата публикации книги */
    @Column(name = "published", nullable = false)
    private LocalDate published;

    /** Количество книг в наличии */
    @Column(name = "number_of", nullable = false)
    private Long numberOf;

    /** Жанр */
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    /** Автор */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    /** Продукты */
    @ManyToMany(mappedBy = "books")
    private List<Product> products = new ArrayList<>();

}
