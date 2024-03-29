package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.entity.CartItemEntity;
import org.shop.com.repository.ProductJpaRepository;

@Mapper(componentModel = "spring", uses = {ProductJpaRepository.class})
public interface CartItemMapper {
    @Mapping(source = "product.id", target = "productId")
    CartItemDto toDto(CartItemEntity cartItemEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItemEntity fromCreateDto(CartItemCreateDto createDto);
}