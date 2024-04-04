package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;
import org.shop.com.mapper.ProductMapper;
import org.shop.com.service.CategoryService;
import org.shop.com.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        ProductDto productDto = new ProductDto(1L, "Laptop", "High-end gaming laptop", new BigDecimal("2500.00"), "image.jpg", null, null, new BigDecimal("2000.00"), 1L);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
    }

    @Test
    void listProductsShouldReturnProducts() throws Exception {
        ProductDto productDto = new ProductDto(1L, "Laptop", "High-end gaming laptop", new BigDecimal("2500.00"), "image.jpg", null, null, new BigDecimal("2000.00"), 1L);

        given(productService.getAll(null, null, null, null, "name")).willReturn(Collections.singletonList(new ProductEntity()));

        given(productMapper.toDto(any())).willReturn(productDto);

        mockMvc.perform(get("/v1/products")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Laptop")));
    }


    @Test
    void getProductByIdShouldReturnProduct() throws Exception {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Laptop");
        productEntity.setDescription("High-end gaming laptop");
        productEntity.setPrice(new BigDecimal("2500.00"));
        productEntity.setImage("image.jpg");
        productEntity.setDiscountPrice(new BigDecimal("2000.00"));

        ProductDto expectedDto = new ProductDto(1L, "Laptop", "High-end gaming laptop", new BigDecimal("2500.00"), "image.jpg", new BigDecimal("2000.00"), 1L);

        given(productService.findById(1L)).willReturn(productEntity);
        given(productMapper.toDto(any())).willReturn(expectedDto);

        mockMvc.perform(get("/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.description", is("High-end gaming laptop")))
                .andExpect(jsonPath("$.price", is(2500.00)))
                .andExpect(jsonPath("$.image", is("image.jpg")))
                .andExpect(jsonPath("$.discountPrice", is(2000.00)));
    }


    @Test
    void createProductShouldReturnCreatedProduct() throws Exception {
        ProductCreateDto productCreateDto = new ProductCreateDto("Laptop", "High-end gaming laptop", new BigDecimal("2500.00"), "image.jpg", 1L);
        ProductEntity createdProductEntity = new ProductEntity();
        createdProductEntity.setId(1L); // Установка ID для имитации создания
        ProductDto expectedDto = new ProductDto(1L, "Laptop", "High-end gaming laptop", new BigDecimal("2500.00"), "image.jpg", null, null, new BigDecimal("2000.00"), 1L);

        given(productService.create(any())).willReturn(createdProductEntity);
        given(productMapper.toDto(any())).willReturn(expectedDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop")));
    }


    @Test
    void updateProductShouldReturnUpdatedProduct() throws Exception {
        ProductDto productDto = new ProductDto( "Updated Laptop", "Updated description", new BigDecimal("2600.00"), "updated_image.jpg", new BigDecimal("2100.00"));
        given(productService.update(any())).willReturn(new ProductEntity());

        mockMvc.perform(put("/v1/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProductShouldReturnNoContent() throws Exception {
        doNothing().when(productService).delete(anyLong());

        mockMvc.perform(delete("/v1/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
