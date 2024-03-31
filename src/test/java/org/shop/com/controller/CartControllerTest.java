package org.shop.com.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private CartCreateDto cartCreateDto;
    private CartDto cartDto;

    @BeforeEach
    void setUp() {
        cartCreateDto = new CartCreateDto();
        cartDto = new CartDto();
    }

    @Test
    void createOrUpdateCart_ReturnsOk() {
        when(cartService.createOrUpdate(any(CartCreateDto.class))).thenReturn(cartDto);

        ResponseEntity<CartDto> response = cartController.createOrUpdate(cartCreateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartDto, response.getBody());
    }

    @Test
    void getCartByUserId_ReturnsOk() {
        Long userId = 1L;
        when(cartService.getByUserId(userId)).thenReturn(cartDto);

        ResponseEntity<CartDto> response = cartController.getByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartDto, response.getBody());
    }

    @Test
    void deleteCart_ReturnsOk() {
        Long cartId = 1L;

        ResponseEntity<Void> response = cartController.delete(cartId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllCarts_ReturnsOk() {
        when(cartService.getAll()).thenReturn(List.of(cartDto));

        ResponseEntity<List<CartDto>> response = cartController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(cartDto, response.getBody().get(0));
    }
}