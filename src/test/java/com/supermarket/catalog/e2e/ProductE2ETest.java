package com.supermarket.catalog.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.repository.ProductRepository;
import com.supermarket.catalog.test_config.TestEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductE2ETest extends TestEnvironment {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createProduct_andFetch_itIsPersistedCorrectly() throws Exception {

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username("e2e_user")
                .password("password")
                .email("e2e@test.com")
                .storeName("E2E Store")
                .build();

        UUID userId = objectMapper.readValue(mockMvc.perform(post("/users-catalog")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
                UUID.class
        );

        CreateProductRequest productRequest = CreateProductRequest.builder()
                .name("Milk")
                .category(Category.DAIRY)
                .price(BigDecimal.valueOf(5.9))
                .supplier("Local Supplier")
                .initialQuantity(10)
                .description("Fresh milk")
                .build();

        UUID productId = objectMapper.readValue(mockMvc.perform(post("/products-catalog")
                                .header("X-User-Id", userId.toString())
                                .header("X-Username", "e2e_user")
                                .header("X-Store-Name", "E2E Store")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productRequest)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                UUID.class
        );

        Product product = productRepository.findById(productId).orElseThrow();

        assertThat(product.getName()).isEqualTo("Milk");
        assertThat(product.getCategory()).isEqualTo(Category.DAIRY);
        assertThat(product.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(5.9));
        assertThat(product.getStockQuantity()).isEqualTo(10);
        assertThat(product.getSupplier()).isEqualTo("Local Supplier");
        assertThat(product.getDescription()).isEqualTo("Fresh milk");
    }
}
