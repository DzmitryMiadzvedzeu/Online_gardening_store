package org.shop.com.service;


import org.shop.com.dto.CategoryCreateDTO;

import org.shop.com.entity.CategoryEntity;


import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryEntity> getAll();

    CategoryEntity getById(Long id);

    Optional<CategoryEntity> findByName(String name);

    CategoryEntity create(CategoryCreateDTO createDTO);

    CategoryEntity edit(Long id, CategoryCreateDTO categoryDTO);

    void delete(Long id);
}