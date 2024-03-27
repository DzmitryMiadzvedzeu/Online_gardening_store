package org.shop.com.controller;


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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartItemControllerTest {

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartItemController cartItemController;

    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartItemController).build();
    }

    @Test
    void addCartItem_Success() throws Exception {
        CartItemCreateDto createDto = new CartItemCreateDto(1L, 5);
        CartItemDto cartItemDto = new CartItemDto(1L, 1L, 5);

        given(cartItemService.addCartItem(any(CartItemCreateDto.class), eq(1L))).willReturn(cartItemDto);

        mockMvc = MockMvcBuilders.standaloneSetup(cartItemController).build();

        mockMvc.perform(post("/cartItems?cartId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void removeCartItem_Success() throws Exception {
        doNothing().when(cartItemService).removeCartItem(1L);
        mockMvc.perform(delete("/cartItems/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCartItemsByCartId_Success() throws Exception {
        List<CartItemDto> cartItems = List.of(new CartItemDto(1L, 1L, 5));
        given(cartItemService.getCartItemsByCartId(1L)).willReturn(cartItems);

        mockMvc.perform(get("/cartItems/cart/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

    @Test
    void updateCartItemQuantity_Success() throws Exception {
        CartItemDto updatedCartItemDto = new CartItemDto(1L, 1L, 10);
        given(cartItemService.updateCartItemQuantity(1L, 10)).willReturn(updatedCartItemDto);

        mockMvc.perform(put("/cartItems/1?quantity=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(10));
    }


}