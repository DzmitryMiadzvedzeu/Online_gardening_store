package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.CategoryCreateDTO;
import org.shop.com.dto.CategoryDTO;
import org.shop.com.entity.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDto(CategoryEntity entity);

    CategoryEntity toEntity(CategoryDTO dto);

    CategoryEntity createDtoToEntity(CategoryCreateDTO createDTO);
}