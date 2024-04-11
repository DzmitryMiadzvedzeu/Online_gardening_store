package org.shop.com.service;

import org.shop.com.dto.UserCreateDto;
import org.shop.com.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserEntity create(UserEntity userEntity);

    UserEntity findById(long id);

    List<UserEntity> getAll();

    UserEntity edit(long id, UserCreateDto userCreateDto);

    void delete(long id);

    Long getCurrentUserId();

    UserEntity getByLogin(String login);
}