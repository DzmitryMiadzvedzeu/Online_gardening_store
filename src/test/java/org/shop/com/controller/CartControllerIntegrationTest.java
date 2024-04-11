package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.security.JwtService;
import org.shop.com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void testCreateOrUpdateCart() throws Exception {
        CartCreateDto cartCreateDto = new CartCreateDto(1L, Collections.emptyList());
        CartDto expectedCartDto = new CartDto(1L, 1L);

        given(cartService.createOrUpdate(any(CartCreateDto.class))).willReturn(expectedCartDto);

        mockMvc.perform(post("/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L));

        Mockito.verify(cartService, Mockito.times(1)).createOrUpdate(any(CartCreateDto.class));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void testGetCartByUserId() throws Exception {
        Long userId = 1L;
        CartDto expectedCartDto = new CartDto(1L, userId);

        given(cartService.getByUserId(userId)).willReturn(expectedCartDto);

        mockMvc.perform(get("/v1/carts/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(userId));

        Mockito.verify(cartService, Mockito.times(1)).getByUserId(userId);
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void testDeleteCart() throws Exception {
        Long cartId = 1L;

        mockMvc.perform(delete("/v1/carts/{cartId}", cartId))
                .andExpect(status().isNoContent());

        Mockito.verify(cartService, Mockito.times(1)).delete(cartId);
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void testGetAllCarts() throws Exception {
        CartDto cartDto1 = new CartDto(1L, 1L);
        CartDto cartDto2 = new CartDto(2L, 2L);

        given(cartService.getAll()).willReturn(Arrays.asList(cartDto1, cartDto2));

        mockMvc.perform(get("/v1/carts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        Mockito.verify(cartService, Mockito.times(1)).getAll();
    }
}