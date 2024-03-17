package org.shop.com.service;


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
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public CategoryEntity createCategory(CategoryCreateDTO createDTO) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(createDTO.getName());

        CategoryEntity categoryEntity = categoryDtoConverter.toEntity(categoryDTO);
        return categoryRepository.save(categoryEntity);
    }
    @Override
    public CategoryEntity editCategory(Long id, CategoryCreateDTO categoryDTO) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDTO.getName());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found."));
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryInvalidArgumentException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}