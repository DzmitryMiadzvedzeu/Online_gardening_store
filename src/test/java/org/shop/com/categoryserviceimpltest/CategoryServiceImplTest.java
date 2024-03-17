package org.shop.com.categoryserviceimpltest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.shop.com.converter.CategoryDtoConverter;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.exceptions.CategoryInvalidArgumentException;
import org.shop.com.repository.CategoryJpaRepository;
import org.shop.com.service.CategoryServiceImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryJpaRepository categoryRepository;

    @Mock
    private CategoryDtoConverter categoryDtoConverter;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void whenGetAllCategories_thenReturnsCategories() {
        CategoryEntity category1 = new CategoryEntity(1L, "Gardening Tools");
        CategoryEntity category2 = new CategoryEntity(2L, "Plant Seeds");
        List<CategoryEntity> expectedCategories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        List<CategoryEntity> result = categoryService.getAllCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Gardening Tools");
        assertThat(result.get(1).getName()).isEqualTo("Plant Seeds");
    }

    @Test
    void whenGetCategoryById_thenReturnsCategory() {
        CategoryEntity category = new CategoryEntity(1L, "Gardening Tools");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryEntity result = categoryService.getCategoryById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Gardening Tools");
    }

    @Test
    void whenCreateCategory_thenCategoryIsCreated() {
        CategoryCreateDTO createDTO = new CategoryCreateDTO("New Tools");
        CategoryEntity categoryEntity = new CategoryEntity(null, "New Tools");
        CategoryEntity savedCategoryEntity = new CategoryEntity(1L, "New Tools");
        CategoryDTO categoryDTO = new CategoryDTO(null, "New Tools");

        when(categoryDtoConverter.toEntity(any(CategoryDTO.class))).thenReturn(categoryEntity);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedCategoryEntity);

        CategoryEntity result = categoryService.createCategory(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Tools");
    }
    @Test
    void whenEditCategory_thenCategoryIsUpdated() {
        Long categoryId = 1L;
        CategoryCreateDTO updateDTO = new CategoryCreateDTO("Updated Tools");
        CategoryEntity existingCategory = new CategoryEntity(categoryId, "Gardening Tools");
        CategoryEntity updatedCategory = new CategoryEntity(categoryId, "Updated Tools");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        when(categoryDtoConverter.toEntity(any(CategoryDTO.class))).thenReturn(updatedCategory);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(updatedCategory);

        CategoryEntity result = categoryService.editCategory(categoryId, updateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updateDTO.getName());

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(any(CategoryEntity.class));
    }
    @Test
    void whenDeleteCategory_withExistingCategory_thenCategoryIsDeleted() {
        Long categoryId = 1L;

        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
    @Test
    void whenDeleteCategory_withNonExistingCategory_thenThrowsException() {
        Long categoryId = 2L;

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryInvalidArgumentException.class, () -> categoryService.deleteCategory(categoryId));

        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository, never()).deleteById(anyLong());
    }

}