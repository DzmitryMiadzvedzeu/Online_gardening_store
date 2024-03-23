package org.shop.com.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.shop.com.converter.UserDtoConverter;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.enums.UserRole;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.repository.UserJpaRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserJpaRepository repository;

    @Mock
    private UserDtoConverter converter;

   // @Mock
   // private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByIdShouldReturnUser() {
        long userId = 1L;
        UserEntity userEntity = new UserEntity("Test Name", "test@example.com",
                "1234567890", "hashedPassword", UserRole.USER);
        when(repository.findById(eq(userId))).thenReturn(Optional.of(userEntity));
        UserEntity foundUser = userService.findById(userId);
        verify(repository).findById(userId);
        assert foundUser == userEntity;
    }

    @Test
    void findByIdShouldThrowUserNotFoundException() {
        long userId = 1L;
        when(repository.findById(eq(userId))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    void createShouldSaveUserSuccessfully() {
        UserCreateDto createDto = new UserCreateDto("Test Name", "test@example.com",
                "1234567890", "password", UserRole.USER);
        UserEntity userEntity = new UserEntity("Test Name", "test@example.com",
                "1234567890", "hashedPassword", UserRole.USER);
        when(converter.toEntity(any(UserDto.class))).thenReturn(userEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
      //  when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        userService.create(createDto);
        verify(repository).save(userEntity);
     //   verify(passwordEncoder).encode("password");
    }

    @Test
    void editUserShouldUpdateUser() {
        long userId = 1L;
        UserCreateDto userDto = new UserCreateDto("UpdatedName",
                "updated@example.com", "9876543210",
                "newPassword", UserRole.USER);
        UserEntity existingUser = new UserEntity("OriginalName",
                "original@example.com", "1234567890",
                "originalPassword", UserRole.USER);
        UserEntity updatedUser = new UserEntity("UpdatedName",
                "updated@example.com", "9876543210",
                "hashedNewPassword", UserRole.USER);
        when(repository.findById(eq(userId))).thenReturn(Optional.of(existingUser));
     //   when(passwordEncoder.encode(eq("newPassword"))).thenReturn("hashedNewPassword");
        when(repository.save(any(UserEntity.class))).thenReturn(updatedUser);
        UserEntity result = userService.editUser(userId, userDto);
        assertNotNull(result);
        assertEquals("UpdatedName", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("9876543210", result.getPhoneNumber());
        assertEquals("hashedNewPassword", result.getPasswordHash());
        assertEquals(UserRole.USER, result.getRole());
        verify(repository, times(1)).findById(eq(userId));
      //  verify(passwordEncoder, times(1)).encode(eq("newPassword"));
        verify(repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void deleteWhenUserExistsShouldCallDeleteById() {
        long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        userService.delete(userId);
        verify(repository).deleteById(userId);
    }

    @Test
    void deleteWhenUserDoesNotExistShouldThrowException() {
        long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        verify(repository, never()).deleteById(anyLong());
    }
}
