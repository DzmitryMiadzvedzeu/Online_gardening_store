package org.shop.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.mapper.UserMapper;
import org.shop.com.repository.UserJpaRepository;
import org.shop.com.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private UserCreateDto userCreateDto;

    @BeforeEach
    void setUp() {
        //тестовые данные, чтобы не писать постоянно
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setPasswordHash("hashedPassword");

        userCreateDto = new UserCreateDto();
        userCreateDto.setName("Test User");
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setPasswordHash("password");
    }

    @Test
    void create_ShouldCreateUser() {
        when(userMapper.userCreateDtoToEntity(any(UserCreateDto.class))).thenReturn(userEntity);
        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity createdUser = userService.create(userCreateDto);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo(userCreateDto.getName());
        verify(userJpaRepository).save(any(UserEntity.class));
    }

    @Test
    void findById_ShouldReturnUser() {
        when(userJpaRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        UserEntity foundUser = userService.findById(1L);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1L);
    }

    @Test
    void findById_ShouldThrowUserNotFoundException() {
        long userId = 2L;
        when(userJpaRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("There is no users with id " + userId);
    }

    @Test
    void getAll_ShouldReturnAllUsers() {
        when(userJpaRepository.findAll()).thenReturn(Arrays.asList(userEntity));

        List<UserEntity> users = userService.getAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void editUser_ShouldEditUser() {
        when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity updatedUser = userService.editUser(1L, userCreateDto);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo(userCreateDto.getName());
    }

    @Test
    void delete_ShouldDeleteUser() {
        when(userJpaRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        doNothing().when(userJpaRepository).deleteById(1L);

        userService.delete(1L);

        verify(userJpaRepository, times(1)).deleteById(1L);
    }
}