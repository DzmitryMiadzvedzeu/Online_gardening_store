package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartItemController.class)
public class CartItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addTest() throws Exception {
        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto(1L, 5);
        long id = 1L;

        given(cartItemService.add(any(CartItemCreateDto.class), anyLong()))
                .willAnswer(invocation -> {
                    CartItemCreateDto dto = invocation.getArgument(0);

                    return new CartItemDto(1L, dto.getProductId(), dto.getQuantity());
                });

        mockMvc.perform(post("/v1/cartItems?cartId=" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(cartItemCreateDto.getProductId()))
                .andExpect(jsonPath("$.quantity").value(cartItemCreateDto.getQuantity()));
    }

    @Test
    public void removeCartItemTest() throws Exception {
        Long cartItemId = 1L;

        mockMvc.perform(delete("/v1/cartItems/{cartItemId}", cartItemId))
                .andExpect(status().isOk());
    }

    @Test
    public void getCartItemsByCartIdTest() throws Exception {
        Long cartId = 1L;

        mockMvc.perform(get("/v1/cartItems/cart/{cartId}", cartId))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCartItemQuantityTest() throws Exception {
        Long cartItemId = 1L;
        Integer newQuantity = 10;

        mockMvc.perform(put("/v1/cartItems/{cartItemId}?quantity={quantity}", cartItemId, newQuantity))
                .andExpect(status().isOk());
    }
}