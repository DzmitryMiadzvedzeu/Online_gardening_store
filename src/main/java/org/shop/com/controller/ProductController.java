package org.shop.com.controller;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.mapper.ProductMapper;
import org.shop.com.service.CategoryService;
import org.shop.com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    private final CategoryService categoryService;


    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper, CategoryService categoryService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> list(@RequestParam(name = "category", required = false) String category,
                                                 @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                                 @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                                 @RequestParam(name = "discount", required = false) BigDecimal discountPrice,
                                                 @RequestParam(name = "sort", required = false) String sort) {
        log.debug("Request received to list all products");
        List<ProductDto> productDto = productService.getAll(category, minPrice, maxPrice, discountPrice, sort).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Return {} products", productDto.size());
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable long id) {
        log.debug("Request sent to get product with id {} ", id);
        ProductDto productDto = productMapper.toDto(productService.findById(id));
        log.debug("Return product with id {} ", id);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductCreateDto productCreateDto) {
        log.debug("Request received to create a new product");
        ProductDto savedDto = productMapper.toDto(productService.create(productCreateDto));
        log.debug("Product created successfully with ID: {}", savedDto.getId());
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

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



