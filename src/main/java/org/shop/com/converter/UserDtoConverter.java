package org.shop.com.converter;

import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter implements UserConverter<UserEntity, UserDto>{
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
    public UserEntity createDtoToEntity(UserCreateDto userCreateDto) {
        UserEntity userEntity = new UserEntity();
                userEntity.setName(userCreateDto.getName());
                userEntity.setEmail(userCreateDto.getEmail());
                userEntity.setPhoneNumber(userCreateDto.getPhoneNumber());
                userEntity.setPasswordHash(userCreateDto.getPasswordHash());
                userEntity.setRole(userCreateDto.getRole());
        return userEntity;
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