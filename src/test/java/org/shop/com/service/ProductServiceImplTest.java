package org.shop.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.repository.ProductJpaRepository;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductJpaRepository repository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductEntity product;
    private ProductCreateDto productCreateDto;
    private CategoryEntity category;

    @BeforeEach
    void setUp() {
        category = new CategoryEntity();
        category.setId(1L);

        product = new ProductEntity();
        product.setId(1L);
        product.setName("Name");
        product.setDescription("Description");
        product.setPrice(new BigDecimal("1000"));
        product.setCategory(category);

        productCreateDto = new ProductCreateDto();
        productCreateDto.setName("Name");
        productCreateDto.setDescription("Description");
        productCreateDto.setPrice(new BigDecimal("1000"));
        productCreateDto.setCategoryId(1L);
    }

    @Test
    void getAll_ShouldReturnProducts() {
        when(repository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(product));
        List<ProductEntity> products = productService.getAll(null, null, null, null, "name");

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(product.getName(), products.get(0).getName());
        verify(repository).findAll(Sort.by("name"));
    }


    @Test
    void findById_ShouldReturnProduct() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        ProductEntity foundProduct = productService.findById(1L);

        assertNotNull(foundProduct);
        assertEquals(product.getName(), foundProduct.getName());

        verify(repository).findById(1L);
    }

    @Test
    void findById_ShouldThrowExceptionWhenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void create_ShouldCreateProduct() {
        when(categoryService.getById(anyLong())).thenReturn(category);
        when(repository.save(any(ProductEntity.class))).thenReturn(product);

        ProductEntity createdProduct = productService.create(productCreateDto);

        assertNotNull(createdProduct);
        assertEquals(product.getName(), createdProduct.getName());

        verify(categoryService).getById(anyLong());
        verify(repository).save(any(ProductEntity.class));
    }

    @Test
    void update_ShouldUpdateProduct() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(product));
        when(repository.save(any(ProductEntity.class))).thenReturn(product);

        product.setName("Updated Name");
        ProductEntity updatedProduct = productService.update(product);

        assertNotNull(updatedProduct);
        assertEquals("Updated Name", updatedProduct.getName());

        verify(repository).findById(anyLong());
        verify(repository).save(any(ProductEntity.class));
    }

    @Test
    void delete_ShouldDeleteProduct() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(repository).delete(any(ProductEntity.class));
        productService.delete(1L);
        verify(repository).findById(anyLong());
        verify(repository).delete(any(ProductEntity.class));
    }
}
