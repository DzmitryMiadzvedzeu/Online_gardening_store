package org.shop.com.service;


import org.shop.com.dto.CategoryCreateDTO;

import org.shop.com.entity.CategoryEntity;


import java.util.List;

public interface CategoryService {

    List<CategoryEntity> getAllCategories();

    CategoryEntity getCategoryById(Long id);

    CategoryEntity createCategory(CategoryCreateDTO createDTO);

    void deleteCategory(Long id);
}