package org.shop.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.UserInvalidArgumentException;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.mapper.UserMapper;
import org.shop.com.security.AuthenticationService;
import org.shop.com.security.model.JwtAuthenticationResponse;
import org.shop.com.security.model.SignInRequest;
import org.shop.com.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final AuthenticationService authenticationService;

    @Operation(summary = "List all users")
    @GetMapping
    public ResponseEntity<List<UserDto>> listAll() {
        log.debug("Fetching all users");
        List<UserDto> userDtos = userService.getAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserEntity userEntity = userMapper.userCreateDtoToEntity(userCreateDto);
        UserEntity createdUserEntity = userService.create(userEntity);
        UserDto createdUserDto = userMapper.toDto(createdUserEntity);
        log.debug("User registered with ID: {}", createdUserDto.getId());
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit an existing user")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> edit(@PathVariable Long id, @Valid @RequestBody UserCreateDto userCreateDto) {
        log.debug("Attempting to edit user with ID: {}", id);
        UserEntity updatedUser = userService.edit(id, userCreateDto);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        log.debug("Attempting to delete user with ID: {}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody SignInRequest request) {
        return authenticationService.authenticate(request);
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