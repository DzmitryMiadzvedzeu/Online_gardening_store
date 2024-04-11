package org.shop.com.service;

import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;

import java.util.List;

public interface CartService {

    CartDto createOrUpdate(CartCreateDto createDto);

    CartDto getByUserId(Long userId);

    void delete(Long productId);

    List<CartDto> getAll();
}