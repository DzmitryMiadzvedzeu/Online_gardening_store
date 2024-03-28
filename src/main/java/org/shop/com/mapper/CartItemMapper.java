package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.entity.CartItemEntity;
import org.shop.com.repository.ProductJpaRepository;

@Mapper(componentModel = "spring", uses = {ProductJpaRepository.class})
public interface CartItemMapper {
    CartItemDto toDto(CartItemEntity cartItemEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    CartItemEntity fromCreateDto(CartItemCreateDto createDto);
}