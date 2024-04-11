package org.shop.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.mapper.ProductMapper;
import org.shop.com.service.CategoryService;
import org.shop.com.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    private final CategoryService categoryService;

    @Operation(summary = "List all products", description = "Fetches a list of products based on optional filter criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))})
    })
    @GetMapping
    public ResponseEntity<List<ProductDto>>
    list(@RequestParam(name = "category", required = false) String category,
         @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
         @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
         @RequestParam(name = "discount", required = false) BigDecimal discountPrice,
         @RequestParam(name = "sort", required = false) String sort) {
        log.debug("Request received to list all products");
        List<ProductDto> productDto = productService.getAll(category, minPrice, maxPrice,
                        discountPrice, sort).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Return {} products", productDto.size());
        return ResponseEntity.ok(productDto);
    }

    @Operation(summary = "Get a product by ID", description = "Fetches a single product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable long id) {
        log.debug("Request sent to get product with id {} ", id);
        ProductDto productDto = productMapper.toDto(productService.findById(id));
        log.debug("Return product with id {} ", id);
        return ResponseEntity.ok(productDto);
    }

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))})
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductCreateDto productCreateDto) {
        log.debug("Request received to create a new product");
        ProductDto savedDto = productMapper.toDto(productService.create(productCreateDto));
        log.debug("Product created successfully with ID: {}", savedDto.getId());
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit a product", description = "Edits an existing product by ID with the provided new details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product edited successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class))})
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> edit(@PathVariable long id, @RequestBody ProductDto productDto) {
        log.debug("Request received to edit product with id {}", id);
        ProductEntity existingProduct = productService.findById(id);

        productMapper.updateEntityFromDto(productDto, existingProduct);

        if (productDto.getCategoryId() > 0) {
            CategoryEntity newCategory = categoryService.getById(productDto.getCategoryId());
            existingProduct.setCategory(newCategory);
        }

        log.debug("Product with id {} edited successfully", id);
        return ResponseEntity.ok(productMapper.toDto(productService.update(existingProduct)));
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.debug("Request received to delete product with id {} ", id);
        productService.delete(id);
        log.debug("Product with id {} deleted successfully ", id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        log.error("Product not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}