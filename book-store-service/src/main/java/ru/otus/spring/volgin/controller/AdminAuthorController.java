package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.volgin.dto.AuthorDto;
import ru.otus.spring.volgin.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер работы с авторами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/authors")
@SecurityRequirement(name = "bearerAuth")
public class AdminAuthorController {

    /** Сервис работы с авторами */
    private final AuthorService authorService;

    /**
     * Возвращает список авторов
     * @return список авторов
     */
    @GetMapping("/")
    public List<AuthorDto> getAuthors() {
        return authorService.findAll();
    }

    /**
     * Сохраняет или обновляет данные автора
     * @param authorDto автор
     * @return автор
     */
    @PostMapping("/")
    public AuthorDto saveOrUpdate(@Valid @RequestBody AuthorDto authorDto) {
        return authorService.saveOrUpdate(authorDto);
    }
}
