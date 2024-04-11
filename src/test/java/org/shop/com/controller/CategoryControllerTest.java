package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.exceptions.CategoryNotFoundException;
import org.shop.com.mapper.CategoryMapper;
import org.shop.com.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void getAllCategoriesTest() throws Exception {
        CategoryEntity categoryOneEntity = new CategoryEntity(1L, "Plants", null);
        CategoryEntity categoryTwoEntity = new CategoryEntity(2L, "Tools", null);
        List<CategoryEntity> categoryEntities = Arrays.asList(categoryOneEntity, categoryTwoEntity);

        CategoryDTO categoryOneDTO = new CategoryDTO(1L, "Plants");
        CategoryDTO categoryTwoDTO = new CategoryDTO(2L, "Tools");
        List<CategoryDTO> categoryDTOs = Arrays.asList(categoryOneDTO, categoryTwoDTO);

        given(categoryService.getAll()).willReturn(categoryEntities);
        given(categoryMapper.toDto(any(CategoryEntity.class))).willReturn(categoryOneDTO, categoryTwoDTO);

        mockMvc.perform(get("/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Plants")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Tools")));

        verify(categoryService, times(1)).getAll();
    }

    @Test
    public void getCategoryByIdTest() throws Exception {
        Long id = 1L;
        CategoryEntity categoryEntity = new CategoryEntity(id, "Plants", null);
        CategoryDTO categoryDTO = new CategoryDTO(id, "Plants");

        given(categoryService.getById(id)).willReturn(categoryEntity);
        given(categoryMapper.toDto(any(CategoryEntity.class))).willReturn(categoryDTO);

        mockMvc.perform(get("/v1/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is("Plants")));
    }

    @Test
    public void createCategoryTest() throws Exception {
        CategoryCreateDTO createDTO = new CategoryCreateDTO("Plants");
        CategoryEntity categoryEntity = new CategoryEntity(1L, "Plants", null);
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Plants");

        given(categoryService.create(any(CategoryCreateDTO.class))).willReturn(categoryEntity);
        given(categoryMapper.toDto(any(CategoryEntity.class))).willReturn(categoryDTO);

        mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Plants")));
    }

    @Test
    public void editCategoryTest() throws Exception {
        Long id = 1L;
        CategoryCreateDTO categoryDTO = new CategoryCreateDTO("UpdatedPlants");
        CategoryEntity updatedEntity = new CategoryEntity(id, "UpdatedPlants", null);
        CategoryDTO updatedDTO = new CategoryDTO(id, "UpdatedPlants");

        given(categoryService.edit(eq(id), any(CategoryCreateDTO.class))).willReturn(updatedEntity);
        given(categoryMapper.toDto(any(CategoryEntity.class))).willReturn(updatedDTO);

        mockMvc.perform(put("/v1/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is("UpdatedPlants")));
    }

    @Test
    public void deleteCategoryTest() throws Exception {
        Long id = 1L;
        doNothing().when(categoryService).delete(id);

        mockMvc.perform(delete("/v1/categories/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void findCategoryByNameTest() throws Exception {
        String name = "Plants";
        CategoryEntity categoryEntity = new CategoryEntity(1L, name, null);
        CategoryDTO categoryDTO = new CategoryDTO(1L, name);

        given(categoryService.findByName(name)).willReturn(Optional.of(categoryEntity));
        given(categoryMapper.toDto(any(CategoryEntity.class))).willReturn(categoryDTO);

        mockMvc.perform(get("/v1/categories/search").param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(name)));
    }

    @Test
    void testGetCategoryByIdNotFound() throws Exception {
        Long id = 1L;
        when(categoryService.getById(id)).thenThrow(new CategoryNotFoundException("Category not found"));

        mockMvc.perform(get("/v1/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Category not found")));
    }
}