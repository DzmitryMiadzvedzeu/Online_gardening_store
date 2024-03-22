package org.shop.com.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shop.com.converter.UserConverter;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.mapper.UserMapper;
import org.shop.com.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
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
        List<UserDto> userDtos = userService.getAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
        UserEntity createUserEntity = userService.create(userCreateDto);
        UserDto createUserDto = userMapper.toDto(createUserEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> editUser(@PathVariable Long id, @Valid @RequestBody UserCreateDto userCreateDto){
        UserEntity updatedUser = userService.editUser(id, userCreateDto);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
