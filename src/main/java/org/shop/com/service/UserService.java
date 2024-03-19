package org.shop.com.service;

import org.shop.com.dto.UserCreateDto;
import org.shop.com.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity create(UserCreateDto userCreateDto);

    UserEntity findById(long id);

    List<UserEntity> getAll();

    UserEntity editUser(long id, UserCreateDto userCreateDto);

    void delete(long id);
}