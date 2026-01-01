package com.supermarket.catalog.controller;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.domain.user.User;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.ProductResponse;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.EntityNotFoundException;
import com.supermarket.catalog.exception.UnauthorizedException;
import com.supermarket.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products-catalog")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestAttribute("authenticatedUser") User user, @RequestBody @Valid CreateProductRequest request) throws InvalidInputException, UnauthorizedException {
        return productService.createProduct(user, request);
    }

    @GetMapping("/{id}")
    public Product get(@RequestAttribute("authenticatedUser") User user, @PathVariable UUID id) throws EntityNotFoundException, UnauthorizedException {
        return productService.getProduct(user, id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID update(@RequestAttribute("authenticatedUser") User user, @PathVariable UUID id,
                       @RequestBody @Valid UpdateProductRequest request) throws InvalidInputException, EntityNotFoundException, UnauthorizedException {
       return productService.updateProduct(user, id, request);
    }

    @PostMapping("/{id}/stock/increase")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID increase(@RequestAttribute("authenticatedUser") User user, @PathVariable UUID id,
                         @RequestBody @Valid StockUpdateRequest request) throws InvalidInputException, EntityNotFoundException, UnauthorizedException {
       return productService.increaseStock(user, id, request);
    }

    @PostMapping("/{id}/stock/decrease")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID decrease(@RequestAttribute("authenticatedUser") User user, @PathVariable UUID id,
                         @RequestBody @Valid StockUpdateRequest request) throws InvalidInputException, EntityNotFoundException, UnauthorizedException {
       return productService.decreaseStock(user, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID delete(@RequestAttribute("authenticatedUser") User user, @PathVariable UUID id) throws EntityNotFoundException, UnauthorizedException {
       return productService.deleteProduct(user, id);
    }
}
