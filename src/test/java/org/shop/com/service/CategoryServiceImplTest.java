package org.shop.com.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.exceptions.CategoryInvalidArgumentException;
import org.shop.com.exceptions.CategoryNotFoundException;
import org.shop.com.repository.CategoryJpaRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryJpaRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(new CategoryEntity(), new CategoryEntity()));

        List<CategoryEntity> result = categoryService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository).findAll();
    }


    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        Long id = 1L;
        CategoryEntity category = new CategoryEntity();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryEntity result = categoryService.getById(id);

        assertNotNull(result);
        verify(categoryRepository).findById(id);
    }
    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(id));
        verify(categoryRepository).findById(id);
    }

    @Test
    void createCategory_ShouldCreateAndReturnCategory() {
        CategoryCreateDTO createDTO = new CategoryCreateDTO();
        createDTO.setName("Test Category");
        CategoryEntity category = new CategoryEntity();
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(category);

        CategoryEntity result = categoryService.create(createDTO);

        assertNotNull(result);
        verify(categoryRepository).save(any(CategoryEntity.class));
    }
    @Test
    void editCategory_WhenCategoryExists_ShouldUpdateAndReturnCategory() {
        Long id = 1L;
        CategoryCreateDTO categoryDTO = new CategoryCreateDTO();
        categoryDTO.setName("Updated Name");
        CategoryEntity category = new CategoryEntity();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(category);

        CategoryEntity result = categoryService.edit(id, categoryDTO);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(categoryRepository).save(category);
        verify(categoryRepository).findById(id);
    }

    @Test
    void editCategory_ShouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.edit(id, new CategoryCreateDTO()));
    }

    @Test
    void deleteCategory_WhenCategoryExists_ShouldDeleteCategory() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);

        categoryService.delete(id);

        verify(categoryRepository).deleteById(id);
    }

    @Test
    void deleteCategory_WhenCategoryDoesNotExist_ShouldThrowException() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(CategoryInvalidArgumentException.class, () -> categoryService.delete(id));
        verify(categoryRepository, never()).deleteById(id);
    }


    @Test
    void findByName_ShouldReturnCategoryOptional() {
        String name = "Test Category";
        when(categoryRepository.findByName(name)).thenReturn(Optional.of(new CategoryEntity()));

        Optional<CategoryEntity> result = categoryService.findByName(name);

        assertTrue(result.isPresent());
        verify(categoryRepository).findByName(name);
    }

}