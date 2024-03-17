package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import org.shop.com.entity.UserEntity;
import org.shop.com.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public  class UserServiceImpl implements UserService {

    private final UserJpaRepository repository;


    @Override
    public UserEntity create(UserEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<UserEntity> getAll() {
        return repository.findAll();
    }
}