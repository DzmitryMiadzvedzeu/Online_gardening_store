package org.shop.com.categorycontrollertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.shop.com.converter.CategoryDtoConverter;
import org.junit.jupiter.api.Test;
import org.shop.com.controller.CategoryController;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryDtoConverter categoryDtoConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCategories() throws Exception {
        CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.setCategoryId(1L);
        categoryEntity1.setName("Shovel");
        CategoryEntity categoryEntity2 = new CategoryEntity();
        categoryEntity2.setCategoryId(2L);
        categoryEntity2.setName("Rake");
        List<CategoryEntity> allEntities = Arrays.asList(categoryEntity1, categoryEntity2);

        List<CategoryDTO> allDtos = allEntities.stream()
                .map(entity -> new CategoryDTO(entity.getCategoryId(), entity.getName()))
                .collect(Collectors.toList());

        when(categoryService.getAllCategories()).thenReturn(allEntities);
        when(categoryDtoConverter.toDto(any(CategoryEntity.class)))
                .thenAnswer(i -> {
                    CategoryEntity entity = i.getArgument(0);
                    return new CategoryDTO(entity.getCategoryId(), entity.getName());
                });

        mockMvc.perform(get("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Shovel")))
                .andExpect(jsonPath("$[1].name", is("Rake")));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);
        categoryEntity.setName("Gardening Tools");
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, "Gardening Tools");

        when(categoryService.getCategoryById(categoryId)).thenReturn(categoryEntity);
        when(categoryDtoConverter.toDto(any(CategoryEntity.class))).thenReturn(categoryDTO);

        mockMvc.perform(get("/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId", is(categoryId.intValue())))
                .andExpect(jsonPath("$.name", is("Gardening Tools")));
    }
    @Test
    void testCreateCategory() throws Exception {
        CategoryCreateDTO createDTO = new CategoryCreateDTO("New Tools");
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(2L);
        categoryEntity.setName("New Tools");
        CategoryDTO categoryDTO = new CategoryDTO(2L, "New Tools");

        when(categoryService.createCategory(any(CategoryCreateDTO.class))).thenReturn(categoryEntity);
        when(categoryDtoConverter.toDto(any(CategoryEntity.class))).thenReturn(categoryDTO);

        mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId", is(2)))
                .andExpect(jsonPath("$.name", is("New Tools")));
    }

    @Test
    void testEditCategory() throws Exception {
        Long categoryId = 1L;
        CategoryCreateDTO categoryDTO = new CategoryCreateDTO("Updated Tools");
        CategoryEntity updatedCategoryEntity = new CategoryEntity();
        updatedCategoryEntity.setCategoryId(categoryId);
        updatedCategoryEntity.setName("Updated Tools");
        CategoryDTO updatedCategoryDTO = new CategoryDTO(categoryId, "Updated Tools");

        when(categoryService.editCategory(eq(categoryId), any(CategoryCreateDTO.class))).thenReturn(updatedCategoryEntity);
        when(categoryDtoConverter.toDto(any(CategoryEntity.class))).thenReturn(updatedCategoryDTO);

        mockMvc.perform(put("/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId", is(categoryId.intValue())))
                .andExpect(jsonPath("$.name", is("Updated Tools")));
    }
//
    @Test
    void testDeleteCategory() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(categoryId);
    }
}
