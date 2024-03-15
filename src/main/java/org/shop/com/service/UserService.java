package org.shop.com.service;

import org.shop.com.entity.UserEntity;

import java.util.List;

public interface UserService {

    void deleteUserById(Long id);

    UserEntity create(UserEntity entity);

    List<UserEntity> getAll();

    void update(UserEntity userEntity);

    void delete(String id);
}