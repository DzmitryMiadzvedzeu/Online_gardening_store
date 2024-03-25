package org.shop.com.service;

import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;

import java.util.List;

public interface CartService {
    CartDto createOrUpdateCart(CartCreateDto createDto);
    CartDto getCartByUserId(Long userId);
    void deleteCart(Long userId);
    List<CartDto> getAllCarts();
}