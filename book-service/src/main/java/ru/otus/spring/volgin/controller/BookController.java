package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для работы с книгами
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    @GetMapping("/books")
    public List<String> getBooks() {
        // TODO
        return List.of("book1", "book1");
    }
}
