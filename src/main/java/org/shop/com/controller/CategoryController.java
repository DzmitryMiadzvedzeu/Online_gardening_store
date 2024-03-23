package org.shop.com.controller;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.exceptions.CategoryInvalidArgumentException;
import org.shop.com.exceptions.CategoryNotFoundException;
import org.shop.com.mapper.CategoryMapper;
import org.shop.com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// http://localhost:8080/v1/categories

@Slf4j
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        log.debug("Received request to list all categories");
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories().stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        log.debug("Received request to fetch category by ID: {}", id);
        CategoryEntity categoryEntity = categoryService.getCategoryById(id);
        return ResponseEntity.ok(CategoryMapper.INSTANCE.toDto(categoryEntity));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryCreateDTO createDTO) {
        log.debug("Received request to create a new category: {}", createDTO.getName());
        CategoryEntity createdCategoryEntity = categoryService.createCategory(createDTO);
        CategoryDTO categoryDTO = CategoryMapper.INSTANCE.toDto(createdCategoryEntity);
        log.debug("Category created successfully with ID: {}", createdCategoryEntity.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> editCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreateDTO categoryDTO) {
        log.debug("Received request to edit category with ID: {}", id);
        CategoryEntity updatedCategory = categoryService.editCategory(id, categoryDTO);
        log.debug("Category with ID: {} updated successfully", updatedCategory.getCategoryId());
        return ResponseEntity.ok(CategoryMapper.INSTANCE.toDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.debug("Received request to delete category with ID: {}", id);
        categoryService.deleteCategory(id);
        log.debug("Category with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<CategoryDTO> findCategoryByName(@RequestParam String name) {
        log.debug("Received request to find category by name: {}", name);
        CategoryEntity categoryEntity = categoryService.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name " + name + " not found."));
        return ResponseEntity.ok(CategoryMapper.INSTANCE.toDto(categoryEntity));
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