package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.repository.UserJpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity create(UserEntity userEntity) {
        log.debug("Attempting to create a user: {}", userEntity.getEmail());
        String hashedPassword = passwordEncoder.encode(userEntity.getPasswordHash());
        userEntity.setPasswordHash(hashedPassword);
        return repository.save(userEntity);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            UserEntity user = getByLogin(username);
            return user.getId();
        } else {
            throw new IllegalArgumentException("The primary authentication object cannot be used to obtain the ID");
        }
    }

    @Override
    public UserEntity getByLogin(String login) {
        return repository.findByName(login)
                .orElseThrow(() -> new UserNotFoundException("User with login {} not found " + login));
    }
}