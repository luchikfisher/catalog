package com.supermarket.catalog.controller;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.dto.product.*;
import com.supermarket.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestBody @Valid CreateProductRequest request) {
        return productService.createProduct(
                request.name(),
                request.category(),
                request.price(),
                request.supplier(),
                request.description(),
                request.initialQuantity()
        );
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable UUID id) {
        Product p = productService.getProduct(id);
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getCategory(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getSupplier(),
                p.getDescription(),
                p.getCreatedAt()
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID id,
                       @RequestBody @Valid UpdateProductRequest request) {

        productService.updateProduct(
                id,
                request.name(),
                request.category(),
                request.price(),
                request.supplier(),
                request.description()
        );
    }

    @PostMapping("/{id}/stock/increase")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void increase(@PathVariable UUID id,
                         @RequestBody @Valid StockUpdateRequest request) {

        productService.increaseStock(id, request.amount());
    }

    @PostMapping("/{id}/stock/decrease")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void decrease(@PathVariable UUID id,
                         @RequestBody @Valid StockUpdateRequest request) {

        productService.decreaseStock(id, request.amount());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }
}