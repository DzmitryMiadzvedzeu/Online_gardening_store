package org.shop.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// http://localhost:8080/v1/categories

@Slf4j
@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "List all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the categories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Categories not found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        log.debug("Received request to list all categories");
        List<CategoryDTO> categoryDTOList = categoryService.getAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOList);
    }

    @Operation(summary = "Get a category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        log.debug("Received request to fetch category by ID: {}", id);
        CategoryEntity categoryEntity = categoryService.getById(id);
        return ResponseEntity.ok(categoryMapper.toDto(categoryEntity));
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))})
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryCreateDTO createDTO) {
        log.debug("Received request to create a new category: {}", createDTO.getName());
        CategoryEntity createdCategoryEntity = categoryService.create(createDTO);
        CategoryDTO categoryDTO = categoryMapper.toDto(createdCategoryEntity);
        log.debug("Category created successfully with ID: {}", createdCategoryEntity.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }

    @Operation(summary = "Edit an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> edit(@PathVariable Long id, @Valid @RequestBody CategoryCreateDTO categoryDTO) {
        log.debug("Received request to edit category with ID: {}", id);
        CategoryEntity updatedCategory = categoryService.edit(id, categoryDTO);
        log.debug("Category with ID: {} updated successfully", updatedCategory.getId());
        return ResponseEntity.ok(categoryMapper.toDto(updatedCategory));
    }

    @Operation(summary = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Received request to delete category with ID: {}", id);
        categoryService.delete(id);
        log.debug("Category with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find a category by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<CategoryDTO> findByName(@RequestParam String name) {
        log.debug("Received request to find category by name: {}", name);
        CategoryEntity categoryEntity = categoryService.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name " + name + " not found."));
        return ResponseEntity.ok(categoryMapper.toDto(categoryEntity)); // Используем внедренный маппер
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryInvalidArgumentException.class)
    public Map<String, String> handleInvalidArgumentException(CategoryInvalidArgumentException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("error", exception.getMessage());
        return map;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public Map<String, String> handleNotFoundException(CategoryNotFoundException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("error", exception.getMessage());
        return map;
    }
}