package org.shop.com.service;

import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;

import java.util.List;

public interface CartItemService {

    CartItemDto add(CartItemCreateDto cartItemCreateDto, Long cartId);

    void remove(Long cartItemId);

    List<CartItemDto> getByCartId(Long cartId);

    CartItemDto updateQuantity(Long cartItemId, Integer quantity);
}