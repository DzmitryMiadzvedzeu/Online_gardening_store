package org.shop.com.converter;


import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoConverter implements Converter<CategoryEntity, CategoryDTO> {

    @Override
    public CategoryDTO toDto(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
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