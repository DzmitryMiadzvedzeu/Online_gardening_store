package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.shop.com.ShopApp;
import org.shop.com.dto.ProductCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShopApp.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void fullProductLifecycleTest() throws Exception {
        ProductCreateDto newProduct = new ProductCreateDto("Laptop", "High-end gaming laptop", new BigDecimal("2500.00"), "image.jpg", 1L);
        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk());

        ProductCreateDto updatedProduct = new ProductCreateDto("Updated Laptop", "Updated high-end gaming laptop", new BigDecimal("2600.00"), "updated_image.jpg", 1L);
        mockMvc.perform(put("/v1/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v1/products/{id}", 1))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/v1/products/{id}", 1))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/products/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
