package com.supermarket.catalog.controller;

import com.supermarket.catalog.domain.product.Product;
import com.supermarket.catalog.dto.product.CreateProductRequest;
import com.supermarket.catalog.dto.product.ProductResponse;
import com.supermarket.catalog.dto.product.StockUpdateRequest;
import com.supermarket.catalog.dto.product.UpdateProductRequest;
import com.supermarket.catalog.exception.InvalidInputException;
import com.supermarket.catalog.exception.EntityNotFoundException;
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
    public UUID create(@RequestBody @Valid CreateProductRequest request)
            throws InvalidInputException {

        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable UUID id)
            throws EntityNotFoundException {

        Product p = productService.getProduct(id);

        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getCategory(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getSupplier(),
                p.getDescription(),
                p.getInsertionTime()
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID update(@PathVariable UUID id,
                       @RequestBody @Valid UpdateProductRequest request)
            throws InvalidInputException, EntityNotFoundException {

       return productService.updateProduct(id, request);
    }

    @PostMapping("/{id}/stock/increase")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID increase(@PathVariable UUID id,
                         @RequestBody @Valid StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException {

       return productService.increaseStock(id, request);
    }

    @PostMapping("/{id}/stock/decrease")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID decrease(@PathVariable UUID id,
                         @RequestBody @Valid StockUpdateRequest request)
            throws InvalidInputException, EntityNotFoundException {

       return productService.decreaseStock(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UUID delete(@PathVariable UUID id)
            throws EntityNotFoundException {

       return productService.deleteProduct(id);
    }
}