package org.shop.com.controller;

import lombok.RequiredArgsConstructor;
import org.shop.com.converter.CategoryConverter;
import org.shop.com.converter.Converter;
import org.shop.com.converter.UserDtoConverter;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.service.UserService;
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


    @GetMapping
    public ResponseEntity<List<UserDto>> listAllUserDto() {
        List<UserDto> userDtos = userService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("register")
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
}