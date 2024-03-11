package org.shop.com.controller;

import org.shop.com.converter.CategoryConverter;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// http://localhost:8080/api/categories
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryConverter categoryConverter;
    @Autowired
    public CategoryController(CategoryService categoryService, CategoryConverter categoryConverter) {
        this.categoryService = categoryService;
        this.categoryConverter = categoryConverter;
    }
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories().stream()
                .map(categoryConverter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryEntity categoryEntity = categoryService.getCategoryById(id);
        if (categoryEntity != null) {
            CategoryDTO categoryDTO = categoryConverter.toDto(categoryEntity);
            return ResponseEntity.ok(categoryDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryCreateDTO createDTO) {
        if (createDTO == null || createDTO.getName() == null || createDTO.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new CategoryDTO(null,"Invalid category data"));
        }
        CategoryEntity createdCategoryEntity = categoryService.createCategory(createDTO);
        CategoryDTO categoryDTO = categoryConverter.toDto(createdCategoryEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}