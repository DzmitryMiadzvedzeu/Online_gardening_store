package org.shop.com.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.exceptions.CategoryInvalidArgumentException;
import org.shop.com.exceptions.CategoryNotFoundException;
import org.shop.com.mapper.CategoryMapper;
import org.shop.com.repository.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJpaRepository categoryRepository;
  //  private final CategoryDtoConverter categoryDtoConverter;

    @Override
    public List<CategoryEntity> getAllCategories() {
        log.debug("Fetching all categories");
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity getById(Long id) {
        log.debug("Fetching category by ID: {}", id);
        return categoryRepository.findById(id).orElseThrow(() ->
                new CategoryNotFoundException("Category with id " + id + " not found."));
    }

    @Override
    public CategoryEntity create(CategoryCreateDTO createDTO) {
        log.debug("Creating category with name: {}", createDTO.getName());
        CategoryEntity categoryEntity = CategoryMapper.INSTANCE.createDtoToEntity(createDTO);
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        log.debug("Category created successfully with ID: {}", savedEntity.getId());
        return savedEntity;
    }
//
    @Override
    public CategoryEntity edit(Long id, CategoryCreateDTO categoryDTO) {
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
    public void delete(Long id) {
        log.debug("Deleting category with ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            log.error("Category with ID {} not found for deletion", id);
            throw new CategoryInvalidArgumentException("Category not found");
        }
        categoryRepository.deleteById(id);
        log.debug("Category with ID: {} deleted successfully", id);
    }
    @Override
    public Optional<CategoryEntity> findByName(String name) {
        log.debug("Searching for category by name: {}", name);
        return categoryRepository.findByName(name);
    }
}