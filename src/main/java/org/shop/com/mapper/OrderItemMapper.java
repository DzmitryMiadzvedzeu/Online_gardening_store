package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.shop.com.dto.OrderItemCreateDto;
import org.shop.com.dto.OrderItemDto;
import org.shop.com.entity.OrderItemEntity;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemDto toDto(OrderItemEntity orderItemEntity);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "quantity", target = "quantity")
    OrderItemEntity createDtoToEntity(OrderItemCreateDto orderItemCreateDto);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "quantity", target = "quantity")
    OrderItemEntity toEntity(OrderItemDto orderItemDto);
}