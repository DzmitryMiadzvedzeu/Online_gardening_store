package org.shop.com.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.UserInvalidArgumentException;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.mapper.UserMapper;
import org.shop.com.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@Slf4j
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//    private final UserConverter<UserEntity, UserDto> converter;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> listAllUserDto() {
        log.debug("Fetching all users");
        List<UserDto> userDtos = userService.getAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
        log.debug("Attempting to register user: {}", userCreateDto);
        UserEntity createUserEntity = userService.create(userCreateDto);
        log.debug("User registered successfully: {}", createUserEntity.getId());
        UserDto createUserDto = userMapper.toDto(createUserEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> editUser(@PathVariable Long id, @Valid @RequestBody UserCreateDto userCreateDto){
        log.debug("Attempting to edit user with ID: {}", id);
        UserEntity updatedUser = userService.editUser(id, userCreateDto);
        log.debug("User with ID: {} edited successfully", id);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        log.debug("Attempting to delete user with ID: {}", id);
        userService.delete(id);
        log.debug("User with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UserInvalidArgumentException.class)
    public ResponseEntity<String> handleUserInvalidArgumentException(UserInvalidArgumentException ex) {
        log.error("Invalid user argument: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
