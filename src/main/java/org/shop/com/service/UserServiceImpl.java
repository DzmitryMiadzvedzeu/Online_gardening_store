package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.mapper.UserMapper;
import org.shop.com.repository.UserJpaRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${onlineShopApp.user.default.userId}")
    private Long defaultUserId;

    private final UserJpaRepository repository;

    //    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserEntity create(UserEntity userEntity) {
        log.debug("Attempting to create a user: {}", userEntity.getEmail());
//        String hashedPassword = passwordEncoder.encode();
        return repository.save(userEntity);
//        UserCreateDto userCreateDto = new UserCreateDto();
//        userCreateDto.setName(userCreateDto.getName());
//        userCreateDto.setEmail(userCreateDto.getEmail());
//        userCreateDto.setPhoneNumber(userCreateDto.getPhoneNumber());
//        userCreateDto.setPasswordHash(userCreateDto.getPasswordHash());
//        return repository.save(userMapper.userCreateDtoToEntity(userCreateDto));
    }

    @Override
    public UserEntity findById(long id) {
        log.debug("Attempting to find a user by ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserNotFoundException("There is no users with id " + id);
                });
    }

    @Override
    public List<UserEntity> getAll() {
        log.debug("Fetching all users");
        return repository.findAll();
    }

    @Override
    public UserEntity edit(long id, UserCreateDto userDto) {
        log.debug("Attempting to edit user with ID: {}", id);
        return repository.findById(id).map(user -> {
            user.setName(userDto.getName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            return repository.save(user);
        }).orElseThrow(() -> {
            log.error("User with id {} not found for editing", id);
            return new UserNotFoundException("User with id " + id + " not found");
        });
    }

    @Override
    public void delete(long id) {
        log.debug("Attempting to delete user with ID: {}", id);
        UserEntity user = repository.findById(id).orElseThrow(() -> {
            log.error("Attempted to delete non-existing user with ID: {}", id);
            return new UserNotFoundException("There is no users with id " + id);
        });
        repository.deleteById(id);
    }

    @Override
    public Long getCurrentUserId() {
        // логика доставания айди юзера из контекста аутентиффикации
        return defaultUserId;
    }
}