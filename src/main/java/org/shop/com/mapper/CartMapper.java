package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.entity.CartEntity;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(CartEntity cartEntity);

}