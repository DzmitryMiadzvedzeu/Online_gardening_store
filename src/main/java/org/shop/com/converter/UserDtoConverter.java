package org.shop.com.converter;

import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter implements Converter<UserEntity, UserDto>{
    @Override
    public UserDto toDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPhoneNumber(),
                userEntity.getPasswordHash(),
                userEntity.getRole()
        );
    }

    @Override
    public UserEntity toEntity(UserDto userDto) {
        return new UserEntity(
                userDto.getName(),
                userDto.getEmail(),
                userDto.getPhoneNumber(),
                userDto.getPasswordHash(),
                userDto.getRole()
        );
    }
}