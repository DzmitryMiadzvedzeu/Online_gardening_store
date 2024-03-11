package org.shop.com.service;


import org.shop.com.converter.CategoryConverter;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
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
        if (createDTO == null || createDTO.getName() == null || createDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Invalid category data");
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(createDTO.getName());

        // Преобразуем CategoryDTO в CategoryEntity
        CategoryEntity categoryEntity = categoryConverter.toEntity(categoryDTO);

        // Сохраняем CategoryEntity
        return categoryRepository.save(categoryEntity);
    }

    @Override
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Category not found");
        }
    }
}