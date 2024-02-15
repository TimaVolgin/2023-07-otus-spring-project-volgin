package ru.otus.spring.volgin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.volgin.KeycloakBaseTest;
import ru.otus.spring.volgin.entity.dto.CredentialsRequest;
import ru.otus.spring.volgin.entity.dto.CreateUserRequest;
import ru.otus.spring.volgin.service.KeycloakAuthorizationService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирует работу авторизации в keycloak")
@AutoConfigureMockMvc
class AuthorizationControllerTest extends KeycloakBaseTest {

    private static final CredentialsRequest USER_CREDENTIALS = new CredentialsRequest("user", "user");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KeycloakAuthorizationService authService;

    @DisplayName("Тестирует получение токена")
    @Test
    void getToken() throws Exception {
        mockMvc.perform(post("/users/token")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_CREDENTIALS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNotEmpty())
                .andExpect(jsonPath("$.refreshExpiresIn").isNotEmpty());
    }

    @DisplayName("Тестирует обновление токена")
    @Test
    void refresh() throws Exception {
        var refreshToken = authService.getToken(USER_CREDENTIALS.getLogin(), USER_CREDENTIALS.getPassword()).getRefreshToken();
        mockMvc.perform(post("/users/refreshToken")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNotEmpty())
                .andExpect(jsonPath("$.refreshExpiresIn").isNotEmpty());
    }

    @DisplayName("Тестирует создание пользователя")
    @Test
    void registerUser() throws Exception {
        var request = new CreateUserRequest("Иван", "Иванович", "test@mail.ru", "qwerty54321!");
        mockMvc.perform(post("/users/createUser")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNotEmpty())
                .andExpect(jsonPath("$.refreshExpiresIn").isNotEmpty());
    }
}
