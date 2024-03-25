package org.shop.com.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.entity.CartEntity;
import org.shop.com.exceptions.CartInvalidArgumentException;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.mapper.CartMapper;
import org.shop.com.repository.CartJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final CartJpaRepository cartRepository;
    private final CartMapper cartMapper;
    @Autowired
    public CartServiceImpl(CartJpaRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Transactional
    @Override
    public CartDto createOrUpdateCart(CartCreateDto createDto) {
        log.debug("Creating or updating cart for user ID: {}", createDto.getUserId());
        if (createDto.getUserId() == null) {
            log.error("User ID must not be null");
            throw new CartInvalidArgumentException("User ID must not be null");
        }

        CartEntity cart = cartRepository.findByUserId(createDto.getUserId())
                .orElseGet(() -> {
                    CartEntity newCart = cartMapper.fromCreateDto(createDto);
                    if (newCart.getUser() == null) {
                        log.error("Invalid User ID: {}", createDto.getUserId());
                        throw new CartInvalidArgumentException("Invalid User ID: " + createDto.getUserId());
                    }
                    return newCart;
                });

        cart = cartRepository.save(cart);
        log.debug("Cart for user ID: {} created or updated successfully", createDto.getUserId());
        return cartMapper.toDto(cart);
    }


    @Override
    public CartDto getCartByUserId(Long userId) {
        log.debug("Retrieving cart for user ID: {}", userId);
        if (userId == null) {
            log.error("User ID must not be null");
            throw new CartInvalidArgumentException("User ID must not be null");
        }

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for user ID: {}", userId);
                    throw new CartNotFoundException("Cart not found for user ID: " + userId);
                });
        return cartMapper.toDto(cart);
    }

    @Override
    public void deleteCart(Long userId) {
        log.debug("Deleting cart with ID: {}", userId);
        if (!cartRepository.existsById(userId)) {
            log.error("Cart with ID {} does not exist", userId);
            throw new CartNotFoundException("Cart with ID " + userId + " does not exist");
        }
        cartRepository.deleteById(userId);
        log.debug("Cart with ID: {} deleted successfully", userId);
    }
    @Override
    public List<CartDto> getAllCarts() {
        log.debug("Retrieving all carts");
        List<CartDto> carts = cartRepository.findAll().stream()
                .map(cartMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Total carts retrieved: {}", carts.size());
        return carts;
    }
}