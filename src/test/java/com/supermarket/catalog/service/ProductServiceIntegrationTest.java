package com.supermarket.catalog.service;

import com.supermarket.catalog.domain.product.Category;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.dto.user.CreateUserRequest;
import com.supermarket.catalog.exception.UserAlreadyExistsException;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.UnauthorizedException;
import com.supermarket.catalog.test_config.TestEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceIntegrationTest extends TestEnvironment {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Test
    void decreasingStockBelowZeroShouldFail() throws UserAlreadyExistsException, EntityNotFoundException, InvalidInputException, UnauthorizedException {
        var userId = userService.createUser(
                CreateUserRequest.builder()
                        .username("user1")
                        .password("password")
                        .email("user1@test.com")
                        .storeName("store-1")
                        .build()
        );

        var user = userService.getUser(userId);
        var productId = productService.createProduct(
                user, CreateProductRequest.builder()
                        .name("Milk")
                        .category(Category.DAIRY)
                        .price(BigDecimal.valueOf(10))
                        .initialQuantity(5)
                        .build()
        );

        assertThatThrownBy(() -> productService.decreaseStock(
                        user, productId,
                        StockUpdateRequest.of(10)
                )
        )
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Stock cannot be negative");
    }
}
