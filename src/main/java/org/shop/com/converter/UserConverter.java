package org.shop.com.converter;

import org.shop.com.dto.UserCreateDto;
import org.shop.com.entity.UserEntity;

public interface UserConverter<Entity, Dto> {

    Dto toDto(Entity entity);

    Entity toEntity(Dto dto);

    Entity createDtoToEntity(UserCreateDto userCreateDto);
}
