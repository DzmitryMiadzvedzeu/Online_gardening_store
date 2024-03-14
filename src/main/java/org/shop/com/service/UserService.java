package org.shop.com.service;

import org.shop.com.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity create(UserEntity entity);

    List<UserEntity> getAll();
}