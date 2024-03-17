package org.shop.com.controller;

import jakarta.validation.Valid;
import org.shop.com.converter.CategoryDtoConverter;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.exceptions.CategoryInvalidArgumentException;
import org.shop.com.exceptions.CategoryNotFoundException;
import org.shop.com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// http://localhost:8080/api/categories
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryDtoConverter categoryDtoConverter;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryDtoConverter categoryDtoConverter) {
        this.categoryService = categoryService;
        this.categoryDtoConverter = categoryDtoConverter;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories().stream()
                .map(categoryDtoConverter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryEntity categoryEntity = categoryService.getCategoryById(id);
        if (categoryEntity != null) {
            CategoryDTO categoryDTO = categoryDtoConverter.toDto(categoryEntity);
            return ResponseEntity.ok(categoryDTO);
        } else {
            throw new CategoryNotFoundException("Category with id " + id + " not found.");
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryCreateDTO createDTO) {
        if (createDTO == null || createDTO.getName() == null || createDTO.getName().isEmpty()) {
            throw new CategoryInvalidArgumentException("Invalid category data");
        }
        CategoryEntity createdCategoryEntity = categoryService.createCategory(createDTO);
        CategoryDTO categoryDTO = categoryDtoConverter.toDto(createdCategoryEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> editCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreateDTO categoryDTO) {
        CategoryEntity updatedCategory = categoryService.editCategory(id, categoryDTO);
        return ResponseEntity.ok(categoryDtoConverter.toDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryInvalidArgumentException.class)
    public Map<String,String> handleInvalidArgumentException(CategoryInvalidArgumentException exception) {
        Map<String,String> map = new HashMap<>();
        map.put("error", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public Map<String,String> handleNotFoundException(CategoryNotFoundException exception) {
        Map<String,String> map = new HashMap<>();
        map.put("error", exception.getMessage());
        return map;
    }
}