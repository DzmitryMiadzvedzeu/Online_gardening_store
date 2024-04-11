package org.shop.com.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() throws Exception {
        mockMvc.perform(get("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))));
    }

    @Test
    void getById_WhenCategoryExists_ShouldReturnCategory() throws Exception {
        mockMvc.perform(get("/v1/categories/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(4)));
    }

    @Test
    void createCategory_ShouldCreateCategory() throws Exception {
        String categoryJson = "{\"name\":\"New Category\"}";
        mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("New Category")));
    }

    @Test
    void editCategory_WhenCategoryExists_ShouldUpdateCategory() throws Exception {
        String updatedCategoryJson = "{\"name\":\"Updated Category Name\"}";
        mockMvc.perform(put("/v1/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCategoryJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Updated Category Name")));

    }

    @Test
    void deleteCategory_WhenCategoryExists_ShouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("/v1/categories/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void findCategoryByName_WhenCategoryExists_ShouldReturnCategory() throws Exception {
        String categoryName = "Garden Shovels";

        mockMvc.perform(get("/v1/categories/search?name=" + categoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(categoryName)));
    }

    @Test
    void findCategoryByName_WhenCategoryDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/v1/categories/search?name=Test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}


