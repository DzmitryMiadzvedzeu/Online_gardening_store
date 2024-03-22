package org.shop.com.service;


import org.shop.com.dto.CategoryCreateDTO;

import org.shop.com.entity.CategoryEntity;


import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryEntity> getAllCategories();

    CategoryEntity getCategoryById(Long id);

    Optional<CategoryEntity> findByName(String name);

    CategoryEntity createCategory(CategoryCreateDTO createDTO);
    CategoryEntity editCategory(Long id, CategoryCreateDTO categoryDTO);

    void deleteCategory(Long id);
}