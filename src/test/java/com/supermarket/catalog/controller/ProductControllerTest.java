package com.supermarket.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.catalog.domain.product.Category;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

        User user = mock(User.class);
        UUID productId = UUID.randomUUID();
        when(productService.createProduct(eq(user), any())).thenReturn(productId);

        CreateProductRequest request = CreateProductRequest.builder()
                .name("Milk")
                .category(Category.DAIRY)
                .price(BigDecimal.valueOf(5.5))
                .supplier("Local Supplier")
                .initialQuantity(3)
                .description("Fresh milk")
                .build();

        mockMvc.perform(post("/products")
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(productId.toString()));
    }

    @Test
    void createProduct_withInvalidBody_returns400() throws Exception {

        User user = mock(User.class);

        CreateProductRequest request = CreateProductRequest.builder()
                .name("")
                .category(null)
                .price(BigDecimal.valueOf(-1))
                .supplier("")
                .initialQuantity(null)
                .description(null)
                .build();

        mockMvc.perform(post("/products")
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void increaseStock_returns204() throws Exception {

        User user = mock(User.class);
        UUID productId = UUID.randomUUID();
        when(productService.increaseStock(eq(user), eq(productId), any()))
                .thenReturn(productId);

        StockUpdateRequest request = StockUpdateRequest.builder()
                .amount(5)
                .build();

        mockMvc.perform(post("/products/{id}/stock/increase", productId)
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void decreaseStock_returns204() throws Exception {

        User user = mock(User.class);
        UUID productId = UUID.randomUUID();
        when(productService.decreaseStock(eq(user), eq(productId), any()))
                .thenReturn(productId);

        StockUpdateRequest request = StockUpdateRequest.builder()
                .amount(2)
                .build();

        mockMvc.perform(post("/products/{id}/stock/decrease", productId)
                        .requestAttr("authenticatedUser", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_returns204() throws Exception {

        User user = mock(User.class);
        UUID productId = UUID.randomUUID();
        when(productService.deleteProduct(eq(user), eq(productId)))
                .thenReturn(productId);

        mockMvc.perform(delete("/products/{id}", productId)
                        .requestAttr("authenticatedUser", user))
                .andExpect(status().isNoContent());
    }
}