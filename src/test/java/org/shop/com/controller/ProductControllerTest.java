package org.shop.com.controller;

import com.fasterxml.classmate.AnnotationOverrides;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;
import org.shop.com.mapper.ProductMapper;
import org.shop.com.service.CategoryService;
import org.shop.com.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }


    @Test
    void getById() {
    }
//
//    @Test
//    public void testListAllProducts() throws Exception {
//        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
//
//        // Подготовка тестовых данных
//        ProductDto productDto = new ProductDto(
//                1L, // id
//                "Test Product", // name
//                "Description for test product", // description
//                new BigDecimal("29.99"), // price
//                "http://example.com/image.jpg", // image
//                LocalDateTime.now(), // createdAt
//                LocalDateTime.now(), // updatedAt
//                new BigDecimal("19.99"), // discountPrice
//                1L // categoryId
//        );
//
//        List<ProductDto> productDtos = Arrays.asList(productDto);
//
//        when(productService.getAll(any(), any(), any(), any(), any())).thenReturn(productDtos);
//
//        mockMvc.perform(get("/v1/products")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(1))) // Проверяем, что список содержит ровно 1 элемент
//                .andExpect(jsonPath("$[0].name", is("Test Product"))) // Проверяем, что имя продукта соответствует ожидаемому
//                .andExpect(jsonPath("$[0].price", is(29.99))); // Проверяем, что цена продукта соответствует ожидаемой
//    }

    @Test
    void addProduct() {
    }

    @Test
    void delete() {
    }

    @Test
    void editProduct() {
    }

    @Test
    void listAllCategories() {
    }
}