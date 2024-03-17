package org.shop.com.controller;
import org.shop.com.converter.ProductConverter;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;
import org.shop.com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductConverter<ProductEntity, ProductDto> productConverter;

    @Autowired
    public ProductController(ProductService productService, ProductConverter<ProductEntity, ProductDto> productConverter) {
        this.productService = productService;
        this.productConverter = productConverter;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable long id) {
        ProductEntity productEntity = productService.findById(id);
        ProductDto productDto = productConverter.toDto(productEntity);
        return ResponseEntity.ok(productDto);
    }


    @GetMapping
    public ResponseEntity<List<ProductDto>> list(@RequestParam(name = "category", required = false) String category,
                                                 @RequestParam(name = "minPrice", required = false) Double minPrice,
                                                 @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                                 @RequestParam(name = "discount", required = false) Boolean discount,
                                                 @RequestParam(name = "sort", required = false) String sort) {
        List<ProductDto> productDtos = productService.getAll(category, minPrice, maxPrice, sort).stream()
                .map(productConverter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductCreateDto productCreateDto) {
        ProductEntity productEntity = productConverter.createDtoToEntity(productCreateDto);
        ProductEntity savedEntity = productService.create(productEntity);
        ProductDto savedDto = productConverter.toDto(savedEntity);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> editProduct(@PathVariable long id, @RequestBody ProductDto productDto) {
        productDto.setId(id); // Устанавливаем ID продукта из пути
        ProductEntity productEntityToUpdate = productConverter.toEntity(productDto);
        ProductEntity updatedProductEntity = productService.update(productEntityToUpdate);
        ProductDto updatedDto = productConverter.toDto(updatedProductEntity);
        return ResponseEntity.ok(updatedDto);
    }


    @GetMapping("/categories")
    public ResponseEntity<List<String>> listAllCategories() {
        List<String> categories = productService.listAllCategories();
        return ResponseEntity.ok(categories);
    }
}


