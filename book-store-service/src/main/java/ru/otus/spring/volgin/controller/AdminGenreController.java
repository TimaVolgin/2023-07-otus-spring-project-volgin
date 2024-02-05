package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.dto.GenreDto;
import ru.otus.spring.volgin.service.GenreService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер работы с жанрами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/genres")
@SecurityRequirement(name = "bearerAuth")
public class AdminGenreController {

    /** Сервис работы с жанрами */
    private final GenreService genreService;

    /**
     * Возвращает список жанров
     * @return жанры
     */
    @GetMapping("/")
    public List<GenreDto> getGenres() {
        return genreService.findAll();
    }

    /**
     * Сохраняет или обновляет жанр
     * @param genre жанр
     * @return жанр
     */
    @PostMapping("/")
    public GenreDto saveOrUpdate(@Valid @RequestBody GenreDto genre) {
        return genreService.saveOrUpdate(genre);
    }
}
