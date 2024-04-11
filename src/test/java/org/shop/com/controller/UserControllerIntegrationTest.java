package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.shop.com.dto.UserCreateDto;
import org.shop.com.dto.UserDto;
import org.shop.com.entity.UserEntity;
import org.shop.com.enums.UserRole;
import org.shop.com.mapper.UserMapper;
import org.shop.com.security.AuthenticationService;
import org.shop.com.security.JwtService;
import org.shop.com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity testUserEntity;
    private UserDto testUserDto;
    private UserCreateDto testUserCreateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testUserCreateDto = new UserCreateDto("Иван",
                "ivan@example.com", "1234567890", "hashedPassword");
        testUserEntity = new UserEntity("Иван",
                "ivan@example.com", "1234567890", "hashedPassword");
        testUserDto = new UserDto(1L, "Иван",
                "ivan@example.com", "1234567890", "hashedPassword",
                UserRole.USER);

        given(userMapper.toDto(any(UserEntity.class))).willReturn(testUserDto);
        given(userMapper.userCreateDtoToEntity(any(UserCreateDto.class)))
                .willReturn(testUserEntity);
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void whenRegisterUser_thenReturns201() throws Exception {
        given(userService.create(any(UserEntity.class))).willReturn(testUserEntity);

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(testUserDto.getName()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void whenGetAllUsers_thenReturns200() throws Exception {
        given(userService.getAll()).willReturn(Arrays.asList(testUserEntity));

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(testUserDto.getName()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void whenEditUser_thenReturns200() throws Exception {
        given(userService.edit(eq(1L), any(UserCreateDto.class))).willReturn(testUserEntity);

        mockMvc.perform(put("/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testUserDto.getName()));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void whenDeleteUser_thenReturns204() throws Exception {
        Mockito.doNothing().when(userService).delete(any(Long.class));

        mockMvc.perform(delete("/v1/users/{id}", 1))
                .andExpect(status().isNoContent());
    }
}