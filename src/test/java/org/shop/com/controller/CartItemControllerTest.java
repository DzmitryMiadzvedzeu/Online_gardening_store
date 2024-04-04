package org.shop.com.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.service.CartItemService;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CartItemControllerTest {

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartItemController cartItemController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartItemController).build();
    }

    @Test
    void addCartItem_Success() throws Exception {
        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto(1L, 5);
        CartItemDto cartItemDto = new CartItemDto(1L, 1L, 5);

        given(cartItemService.add(any(CartItemCreateDto.class), eq(1L))).willReturn(cartItemDto);

        mockMvc.perform(post("/v1/cartItems?cartId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void removeCartItem_Success() throws Exception {
        mockMvc.perform(delete("/v1/cartItems/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getByCartId_Success() throws Exception {
        CartItemDto cartItemDto = new CartItemDto(1L, 1L, 5);
        given(cartItemService.getByCartId(1L)).willReturn(Arrays.asList(cartItemDto));

        mockMvc.perform(get("/v1/cartItems/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

    @Test
    void updateQuantity_Success() throws Exception {
        CartItemDto cartItemDto = new CartItemDto(1L, 1L, 10);
        given(cartItemService.updateQuantity(eq(1L), eq(10))).willReturn(cartItemDto);

        mockMvc.perform(put("/v1/cartItems/1?quantity=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(10));
    }
}