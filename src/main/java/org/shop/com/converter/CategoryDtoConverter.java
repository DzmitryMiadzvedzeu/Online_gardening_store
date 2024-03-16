package org.shop.com.converter;


import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryDtoConverter implements Converter<CategoryEntity, CategoryDTO> {

    private final ProductDtoConverter productDtoConverter;

    @Autowired
    public CategoryDtoConverter(ProductDtoConverter productDtoConverter) {
        this.productDtoConverter = productDtoConverter;
    }

    @Override
    public CategoryDTO toDto(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(entity.getCategoryId());
        dto.setName(entity.getName());

        if (entity.getProducts() != null) {
            dto.setProducts(entity.getProducts().stream()
                    .map(productDtoConverter::toDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    public CategoryEntity toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());

        return entity;
    }
}