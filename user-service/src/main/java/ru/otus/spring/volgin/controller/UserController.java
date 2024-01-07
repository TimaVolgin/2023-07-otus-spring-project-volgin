package ru.otus.spring.volgin.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для работы с пользователями
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @GetMapping("/users")
    public List<String> getUsers() {
        // TODO
        return List.of("user1", "user1");
    }
}
