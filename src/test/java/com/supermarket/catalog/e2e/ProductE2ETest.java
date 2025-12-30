package com.supermarket.catalog.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.testinfra.BaseIntegrationTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("e2e")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductE2ETest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_andFetch_itIsPersistedCorrectly() throws Exception {

        // ---------- 1. Create user (and store) ----------
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username("e2e_user")
                .password("password")
                .email("e2e@test.com")
                .storeName("E2E Store")
                .build();

        String userResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID userId = objectMapper.readValue(userResponse, UUID.class);

        // ---------- 2. Create product ----------
        CreateProductRequest productRequest = CreateProductRequest.builder()
                .name("Milk")
                .category(Category.DAIRY)
                .price(BigDecimal.valueOf(5.9))
                .supplier("Local Supplier")
                .initialQuantity(10)
                .description("Fresh milk")
                .build();

        String productResponse = mockMvc.perform(post("/products")
                        .header("X-User-Id", userId.toString())
                        .header("X-Username", "e2e_user")
                        .header("X-Store-Name", "E2E Store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID productId = objectMapper.readValue(productResponse, UUID.class);

        // ---------- 3. Fetch product and verify persisted state ----------
        mockMvc.perform(get("/products/{id}", productId)
                        .header("X-User-Id", userId.toString())
                        .header("X-Username", "e2e_user")
                        .header("X-Store-Name", "E2E Store"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Milk"))
                .andExpect(jsonPath("$.category").value("DAIRY"))
                .andExpect(jsonPath("$.price").value(5.9))
                .andExpect(jsonPath("$.stockQuantity").value(10))
                .andExpect(jsonPath("$.supplier").value("Local Supplier"))
                .andExpect(jsonPath("$.description").value("Fresh milk"));
    }
}