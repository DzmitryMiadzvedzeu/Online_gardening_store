package org.shop.com.controller;
import org.shop.com.converter.ProductConverter;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;
import org.shop.com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductConverter<ProductEntity, ProductDto> productConverter;
    //mapstruct?

    @GetMapping("/{id}")
    public ProductEntity getById(@PathVariable long id) {
        return productService.findById(id);
    }

    @GetMapping
    public List<ProductDto> list(@RequestParam(name = "category", required = false) String category,
                                 @RequestParam(name = "minPrice", required = false) Double minPrice,
                                 @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                 @RequestParam(name = "discount", required = false) Boolean discount,
                                 @RequestParam(name = "sort", required = false) String sort) {

        List<ProductEntity> productList = productService.getAll(category, minPrice, maxPrice, discount, sort);
        return productList.stream()
                .map(productConverter::toDto)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductDto addProduct(@RequestBody ProductCreateDto productDto) {
        ProductEntity productEntity = productConverter.createDtoToEntity(productDto);
        ProductEntity entity = productService.create(productEntity);
        return productConverter.toDto(entity);
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable long id) {
       productService.delete(id);
    }
}

