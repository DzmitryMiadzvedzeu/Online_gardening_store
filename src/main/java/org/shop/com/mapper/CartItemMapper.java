package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.entity.CartItemEntity;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);
    CartItemDto toDto(CartItemEntity cartItemEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true) // Это поле будет заполняться отдельно, так как CartEntity не передается в DTO
    CartItemEntity fromCreateDto(CartItemCreateDto createDto);
}