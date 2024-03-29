package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.OrderItemCreateDto;
import org.shop.com.dto.OrderItemDto;
import org.shop.com.entity.OrderItemEntity;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "product.id", target = "productId")
    OrderItemDto toDto(OrderItemEntity orderItemEntity);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(target = "priceAtPurchase", ignore = true)
    OrderItemEntity createDtoToEntity(OrderItemCreateDto orderItemCreateDto);
}
