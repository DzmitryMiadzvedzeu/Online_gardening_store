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

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void getAllCategoriesTest() throws Exception {

        CategoryEntity categoryOneEntity = new CategoryEntity(1L, "Plants");
        CategoryEntity categoryTwoEntity = new CategoryEntity(2L, "Tools");
        List<CategoryEntity> categoryEntityList = Arrays.asList(categoryOneEntity, categoryTwoEntity);

        given(categoryService.getAll()).willReturn(categoryEntityList);


        mockMvc.perform(get("/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(categoryOneEntity.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(categoryOneEntity.getName())))
                .andExpect(jsonPath("$[1].id", is(categoryTwoEntity.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(categoryTwoEntity.getName())));


        verify(categoryService, times(1)).getAll();
    }

    @Test
    public void getCategoryByIdTest() throws Exception {

        Long id = 1L;
        CategoryDTO category = new CategoryDTO(id, "Plants");

        given(categoryService.getById(id))
                .willReturn(new CategoryEntity(id, category.getName(), null));

        mockMvc.perform(get("/v1/categories/{id}", id))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is(category.getName())));
    }

    @Test
    public void createCategoryTest() throws Exception {

        CategoryCreateDTO createDTO = new CategoryCreateDTO("Plants");
        CategoryDTO returnedDTO = new CategoryDTO(1L, "Plants");

        given(categoryService.create(any(CategoryCreateDTO.class)))
                .willReturn(new CategoryEntity(1L, "Plants", null));

        mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(returnedDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(returnedDTO.getName())));
    }

    @Test
    public void editCategoryTest() throws Exception {

        Long id = 1L;
        CategoryCreateDTO updateDTO = new CategoryCreateDTO("UpdatedPlants");
        CategoryDTO updatedDTO = new CategoryDTO(id, "UpdatedPlants");

        given(categoryService.edit(eq(id), any(CategoryCreateDTO.class)))
                .willReturn(new CategoryEntity(id, "UpdatedPlants", null));


        mockMvc.perform(put("/v1/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedDTO.getName())));
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
        String name = "Tech";
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Plants");

        given(categoryService.findByName(name)).willReturn(Optional.of(new CategoryEntity(1L, "Plants", null)));

        mockMvc.perform(get("/v1/categories/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(categoryDTO.getName())));
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