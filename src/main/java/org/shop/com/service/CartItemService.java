package org.shop.com.service;

import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;

import java.util.List;

public interface CartItemService {

    CartItemDto addCartItem(CartItemCreateDto cartItemCreateDto, Long cartId);

    void removeCartItem(Long cartItemId);

    List<CartItemDto> getCartItemsByCartId(Long cartId);

    CartItemDto updateCartItemQuantity(Long cartItemId, Integer quantity);
}