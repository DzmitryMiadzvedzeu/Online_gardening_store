package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "deliveryMethod", target = "deliveryMethod")
    @Mapping(source = "contactPhone", target = "contactPhone")
    OrderDto toDto(OrderEntity orderEntity);

    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "deliveryMethod", target = "deliveryMethod")
    @Mapping(source = "contactPhone", target = "contactPhone")
    OrderEntity toEntity(OrderDto orderDto);

    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "deliveryMethod", target = "deliveryMethod")
    @Mapping(source = "contactPhone", target = "contactPhone")
    @Mapping(target = "orderItems", source = "items")
    OrderEntity orderCreateDtoToEntity(OrderCreateDto orderCreateDto);
}