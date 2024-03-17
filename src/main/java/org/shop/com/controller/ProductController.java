package org.shop.com.controller;
import org.shop.com.converter.ProductConverter;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//                                 @RequestParam(name = "discount", required = false) Boolean discount,
                                 @RequestParam(name = "sort", required = false) String sort) {

        List<ProductEntity> productList = productService.getAll(category, minPrice, maxPrice, sort);
        return productList.stream()
                .map(productConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductCreateDto productDto) {
        ProductEntity productEntity = productConverter.createDtoToEntity(productDto);
        ProductEntity entity = productService.create(productEntity);
        ProductDto dto = productConverter.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @DeleteMapping("/{id}")
    public void delete (@PathVariable long id) {
       productService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editProduct (@PathVariable long id, @RequestBody ProductDto productDto) {
        ProductEntity product = productService.findById(id);
        if (product == null) {
            throw new ProductNotFoundException("Can't find product with id " + id);
        }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setImage(productDto.getImage());
        ProductEntity updatedProduct = productService.update(product);
        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during update. Try again");
        } else{
            ResponseEntity.ok("Product updated successfully");
        }
        return null;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> listAllCategories () {
        if (productService.listAllCategories().equals(null)) {
            ResponseEntity.status(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(productService.listAllCategories());
    }
}


