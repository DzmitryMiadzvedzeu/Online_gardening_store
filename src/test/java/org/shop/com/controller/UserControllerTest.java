package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.enums.UserRole;
import org.shop.com.mapper.UserMapper;
import org.shop.com.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void listAllUserDto_ShouldReturnUserList() throws Exception {
        UserDto userDto = new UserDto(1L, "Test", "test@example.com",
                "1234567890", "hash", UserRole.USER);
        given(userService.getAll()).willReturn(List.of(new UserEntity()));
        given(userMapper.toDto(any(UserEntity.class))).willReturn(userDto);
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(userDto))));
    }

    @Test
    void registerUser_ShouldCreateUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto("Test", "test@example.com",
                "1234567890", "hash");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Test");
        userEntity.setEmail("test@example.com");
        userEntity.setPhoneNumber("1234567890");
        userEntity.setPasswordHash("hash");
        userEntity.setRole(UserRole.USER);

        UserDto userDto = new UserDto(1L, "Test", "test@example.com",
                "1234567890", "hash", UserRole.USER);

        lenient().when(userMapper.userCreateDtoToEntity(any())).thenReturn(userEntity);
        lenient().when(userService.create(any())).thenReturn(userEntity);
        lenient().when(userMapper.toDto(any())).thenReturn(userDto);

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService).create(eq(userEntity));  // Проверка вызова создания пользователя
    }

    @Test
    void editUser_ShouldUpdateUser_WithLimitedFields() throws Exception {
        long userId = 1L;
        UserCreateDto userUpdateDto = new UserCreateDto("UpdatedName",
                null, "9876543210", null);

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setName("OriginalName");
        existingUser.setEmail("original@example.com");
        existingUser.setPhoneNumber("1234567890");
        existingUser.setPasswordHash("originalHash");
        existingUser.setRole(UserRole.USER);

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userId);
        updatedUser.setName("UpdatedName");
        updatedUser.setEmail("original@example.com");
        updatedUser.setPhoneNumber("9876543210");
        updatedUser.setPasswordHash("originalHash");
        updatedUser.setRole(UserRole.USER);

        UserDto updatedUserDto = new UserDto(userId, "UpdatedName",
                "original@example.com", "9876543210",
                "originalHash", UserRole.USER);
        given(userService.edit(eq(userId), any(UserCreateDto.class))).willReturn(updatedUser);
        given(userMapper.toDto(any(UserEntity.class))).willReturn(updatedUserDto);
        mockMvc.perform(put("/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUserDto)));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        long userId = 1L;
        mockMvc.perform(delete("/v1/users/{id}", userId))
                .andExpect(status().isNoContent());
        verify(userService).delete(userId);
    }
}




