package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.entity.CartEntity;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDto toDto(CartEntity cartEntity);

    @Mapping(target = "id", ignore = true)
    CartEntity fromCreateDto(CartCreateDto createDto);
}