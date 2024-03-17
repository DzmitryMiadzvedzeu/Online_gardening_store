package org.shop.com.controller;

import lombok.RequiredArgsConstructor;
import org.shop.com.converter.Converter;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Converter<UserEntity, UserDto> converter;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreateDto userCreateDto) {
        UserEntity userEntity = convertCreateDtoToEntity(userCreateDto);
        UserEntity createdUser = userService.create(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created with id: " + createdUser.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editUser(@PathVariable String id, @RequestBody UserCreateDto userCreateDto) {
        UserEntity userEntity = convertCreateDtoToEntity(userCreateDto);
        userEntity.setId(Long.parseLong(id));
        userService.update(userEntity);
        return ResponseEntity.ok("User edited");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.ok("User deleted");
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> listAllUserDto() {
        List<UserDto> userDtos = userService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
        UserEntity userEntity = convertCreateDtoToEntity(userCreateDto);
        UserEntity createUserEntity = userService.create(userEntity);
        UserDto createUserDto = converter.toDto(createUserEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserDto);
    }

    private UserEntity convertCreateDtoToEntity(UserCreateDto userCreateDto) {
        return new UserEntity(
                userCreateDto.getName(),
                userCreateDto.getEmail(),
                userCreateDto.getPhoneNumber(),
                userCreateDto.getPasswordHash(),
                userCreateDto.getRole()
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(UserController.class, args);
    }
}
