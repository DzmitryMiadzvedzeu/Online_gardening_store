package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {



    UserDto toDto(UserEntity userEntity);


    UserEntity toEntity(UserDto userDto);


    UserEntity userCreateDtoToEntity(UserCreateDto userCreateDto);
}
