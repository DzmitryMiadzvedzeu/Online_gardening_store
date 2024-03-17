package org.shop.com.service;


import lombok.extern.slf4j.Slf4j;
import org.shop.com.converter.CategoryDtoConverter;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.exceptions.CategoryInvalidArgumentException;
import org.shop.com.exceptions.CategoryNotFoundException;
import org.shop.com.repository.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJpaRepository categoryRepository;
    private final CategoryDtoConverter categoryDtoConverter;

    @Autowired
    public CategoryServiceImpl(CategoryJpaRepository categoryJpaRepository, CategoryDtoConverter categoryDtoConverter) {
        this.categoryRepository = categoryJpaRepository;
        this.categoryDtoConverter = categoryDtoConverter;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        log.debug("Fetching all categories");
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity getCategoryById(Long id) {
        log.debug("Fetching category by ID: {}", id);
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Category with ID {} not found", id);
            return new CategoryNotFoundException("Category with id " + id + " not found.");
        });
    }

    @Override
    public CategoryEntity createCategory(CategoryCreateDTO createDTO) {
        log.debug("Creating category with name: {}", createDTO.getName());
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(createDTO.getName());

        CategoryEntity categoryEntity = categoryDtoConverter.toEntity(categoryDTO);
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        log.debug("Category created successfully with ID: {}", savedEntity.getCategoryId());
        return savedEntity;
    }

    @Override
    public CategoryEntity editCategory(Long id, CategoryCreateDTO categoryDTO) {
        log.debug("Editing category with ID: {}", id);
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDTO.getName());
            CategoryEntity updatedCategory = categoryRepository.save(category);
            log.debug("Category with ID: {} updated successfully", id);
            return updatedCategory;
        }).orElseThrow(() -> {
            log.error("Category with ID {} not found for update", id);
            return new CategoryNotFoundException("Category with id " + id + " not found.");
        });
    }

    @Override
    public void deleteCategory(Long id) {
        log.debug("Deleting category with ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            log.error("Category with ID {} not found for deletion", id);
            throw new CategoryInvalidArgumentException("Category not found");
        }
        categoryRepository.deleteById(id);
        log.debug("Category with ID: {} deleted successfully", id);
    }
}