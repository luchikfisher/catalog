package com.supermarket.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.domain.store.Store;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.service.ProductService;
import com.supermarket.catalog.validation.HeaderUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private HeaderUserValidator headerUserValidator;

    @BeforeEach
    void setUp() throws Exception {
        doReturn(true)
                .when(headerUserValidator)
                .preHandle(any(), any(), any());
    }


    @Test
    void createProduct_returns201() throws Exception {

        User user = buildUser();
        UUID productId = UUID.randomUUID();
        when(productService.createProduct(eq(user), any())).thenReturn(productId);

        CreateProductRequest request = new CreateProductRequest(
                "Milk",
                Category.DAIRY,
                BigDecimal.valueOf(5.5),
                "Local Supplier",
                3,
                "Fresh milk"
        );

        mockMvc.perform(post("/products")
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(productId.toString()));
    }

    @Test
    void createProduct_withInvalidBody_returns400() throws Exception {

        User user = buildUser();
        CreateProductRequest request = new CreateProductRequest(
                "",
                null,
                BigDecimal.valueOf(-1),
                "",
                null,
                null
        );

        mockMvc.perform(post("/products")
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void increaseStock_returns204() throws Exception {

        User user = buildUser();
        UUID productId = UUID.randomUUID();
        when(productService.increaseStock(eq(user), eq(productId), any()))
                .thenReturn(productId);

        StockUpdateRequest request = new StockUpdateRequest(5);

        mockMvc.perform(post("/products/{id}/stock/increase", productId)
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void decreaseStock_returns204() throws Exception {

        User user = buildUser();
        UUID productId = UUID.randomUUID();
        when(productService.decreaseStock(eq(user), eq(productId), any()))
                .thenReturn(productId);

        StockUpdateRequest request = new StockUpdateRequest(2);

        mockMvc.perform(post("/products/{id}/stock/decrease", productId)
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_returns204() throws Exception {

        User user = buildUser();
        UUID productId = UUID.randomUUID();
        when(productService.deleteProduct(eq(user), eq(productId)))
                .thenReturn(productId);

        mockMvc.perform(delete("/products/{id}", productId)
                        .requestAttr("authenticatedUser", user))
                .andExpect(status().isNoContent());
    }

    private User buildUser() {
        Store store = Store.builder()
                .id(UUID.randomUUID())
                .name("Downtown")
                .insertionTime(Instant.parse("2024-01-01T00:00:00Z"))
                .build();

        return User.builder()
                .id(UUID.randomUUID())
                .username("user")
                .password("pass")
                .email("user@example.com")
                .store(store)
                .insertionTime(Instant.parse("2024-01-02T00:00:00Z"))
                .build();
    }
}
