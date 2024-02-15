package ru.otus.spring.volgin.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.volgin.KeycloakBaseTest;
import ru.otus.spring.volgin.service.KeycloakAdminService;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирует работу с keycloak для администратора")
@AutoConfigureMockMvc
class AdminControllerTest extends KeycloakBaseTest {

    private static final String USER_ID = "36c7648d-1e1f-4dfb-9242-93eaf831c870";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private KeycloakAdminService keycloakAdminService;

    @DisplayName("Тестирование получения пользователя")
    @WithMockUser(roles = "ADMIN")
    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/admin/users/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value("user"));
    }

    @DisplayName("Тестирование получения пользователей")
    @WithMockUser(roles = "ADMIN")
    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(3)));
    }

    @DisplayName("Тестирование получения групп пользователей")
    @WithMockUser(roles = "ADMIN")
    @Test
    void getGroups() throws Exception {
        mockMvc.perform(get("/admin/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @DisplayName("Тестирование установки группы пользователя")
    @WithMockUser(roles = "ADMIN")
    @Test
    void setUserGroup() throws Exception {
        var groupToSet = "\"EDITORS\"";
        var groupToSetStr = groupToSet.replaceAll("^\"|\"$", "").toLowerCase();
        var groupEditor = keycloakAdminService.getGroups().stream().filter(group -> group.getName().equalsIgnoreCase(groupToSetStr)).findFirst();
        assertTrue(groupEditor.isPresent());
        assertFalse(keycloakAdminService.getUser(USER_ID).getGroups().contains(groupToSetStr));
        mockMvc.perform(post("/admin/users/{userId}/group", USER_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupToSet))
                .andExpect(status().isOk());
        assertTrue(keycloakAdminService.getUser(USER_ID).getGroups().contains(groupToSetStr));
    }
}
