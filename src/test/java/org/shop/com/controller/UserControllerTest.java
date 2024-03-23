package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
//import org.shop.com.converter.UserConverter;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.enums.UserRole;
import org.shop.com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
//    private UserConverter converter;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void listAllUserDto_ShouldReturnUserList() throws Exception {
//        List<UserDto> users = List.of(new UserDto(
//                1L,
//                "Test",
//                "test@example.com",
//                "1234567890",
//                "hash",
//                UserRole.USER));
//        when(userService.getAll()).thenReturn(List.of(new UserEntity()));
//        when(converter.toDto(any(UserEntity.class))).thenReturn(users.get(0));
//
//        mockMvc.perform(get("/v1/users"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[{'id':1," +
//                        "'name':'Test'," +
//                        "'email':'test@example.com'," +
//                        "'phoneNumber':'1234567890'," +
//                        "'passwordHash':'hash'," +
//                        "'role':'USER'}]"));
//    }
//
//    @Test
//    void registerUser_ShouldCreateUser() throws Exception {
//        UserCreateDto createDto = new UserCreateDto("Test", "test@example.com",
//                "1234567890", "hash", UserRole.USER);
//        UserEntity createdUser = new UserEntity("Test", "test@example.com",
//                "1234567890", "hash", UserRole.USER);
//        UserDto createdUserDto = new UserDto(1L, "Test", "test@example.com",
//                "1234567890", "hash", UserRole.USER);
//
//        when(userService.create(any(UserCreateDto.class))).thenReturn(createdUser);
//        when(converter.toDto(any(UserEntity.class))).thenReturn(createdUserDto);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andExpect(status().isCreated())
//                .andExpect(content().json("{\"id\":1," +
//                        "\"name\":\"Test\"," +
//                        "\"email\":\"test@example.com\"," +
//                        "\"phoneNumber\":\"1234567890\"," +
//                        "\"passwordHash\":\"hash\"," +
//                        "\"role\":\"USER\"}"));
//    }
//
//    @Test
//    void editUser_ShouldUpdateUser() throws Exception {
//        long userId = 1L;
//        UserCreateDto userUpdateDto = new UserCreateDto("UpdatedName",
//                "updated@example.com", "9876543210",
//                "newHash", UserRole.ADMIN);
//        UserEntity updatedUser = new UserEntity("UpdatedName", "updated@example.com",
//                "9876543210", "newHash", UserRole.ADMIN);
//        UserDto updatedUserDto = new UserDto(userId, "UpdatedName",
//                "updated@example.com", "9876543210",
//                "newHash", UserRole.ADMIN);
//
//        when(userService.editUser(eq(userId), any(UserCreateDto.class))).thenReturn(updatedUser);
//        when(converter.toDto(any(UserEntity.class))).thenReturn(updatedUserDto);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userUpdateDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(updatedUserDto)));
//    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        long userId = 1L;
        mockMvc.perform(delete("/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).delete(userId);
    }
}




