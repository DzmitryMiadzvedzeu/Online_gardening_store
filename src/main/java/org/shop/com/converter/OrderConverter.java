package org.shop.com.converter;

import org.shop.com.dto.OrderCreateDto;

public interface OrderConverter<Entity,Dto> {

    Dto toDto(Entity entity);

    Entity toEntity(Dto dto);

    Entity createDtoToEntity(OrderCreateDto orderCreateDto);
}

