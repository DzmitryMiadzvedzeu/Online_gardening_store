package org.shop.com.service;

import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.mapper.UserMapper;
import org.shop.com.repository.UserJpaRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserJpaRepository repository;
//    private final UserDtoConverter converter;
//    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserJpaRepository repository, UserMapper userMapper) {
        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserEntity create(UserCreateDto createDto) {
        log.debug("Attempting to create a user: {}", createDto.getEmail());
//        String hashedPassword = passwordEncoder.encode();
        UserDto userDto = new UserDto();
        userDto.setName(createDto.getName());
        userDto.setEmail(createDto.getEmail());
        userDto.setPhoneNumber(createDto.getPhoneNumber());
        userDto.setPasswordHash(createDto.getPasswordHash());
        userDto.setRole(createDto.getRole());
        UserEntity savedUserEntity = repository.save(userMapper.toEntity(userDto));
        log.debug("User created successfully with ID: {}", savedUserEntity.getId());
        return savedUserEntity;
    }

    @Override
    public UserEntity findById(long id) {
        log.debug("Attempting to find a user by ID: {}", id);
        return repository.findById(id)
                .orElseThrow(()-> {
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
    public UserEntity editUser(long id, UserCreateDto userDto) {
        log.debug("Attempting to edit user with ID: {}", id);
        return repository.findById(id).map(user -> {
            user.setName(userDto.getName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            log.debug("User with ID: {} edited successfully", id);
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
        log.debug("User with ID: {} deleted successfully", id);
    }

}