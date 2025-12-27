package com.supermarket.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.dto.user.UpdateUserRequest;
import com.supermarket.catalog.service.UserService;
import com.supermarket.catalog.validation.HeaderUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private HeaderUserValidator headerUserValidator;

    @BeforeEach
    void setUp() throws Exception {
        doReturn(true)
                .when(headerUserValidator)
                .preHandle(any(), any(), any());
    }

    @Test
    void createUser_returns201() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.createUser(any())).thenReturn(userId);

        CreateUserRequest request = CreateUserRequest.builder()
                .username("john_doe")
                .password("secret")
                .email("john@example.com")
                .storeName("Downtown")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(userId.toString()));
    }

    @Test
    void createUser_withInvalidEmail_returns400() throws Exception {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("john")
                .password("pass")
                .email("not-an-email")
                .storeName("Downtown")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_returns204() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.updateUser(eq(userId), any()))
                .thenReturn(userId);

        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("new_username")
                .password("new_password")
                .email("new@mail.com")
                .build();

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_returns204() throws Exception {

        UUID userId = UUID.randomUUID();
        when(userService.deleteUser(userId)).thenReturn(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
    }
}