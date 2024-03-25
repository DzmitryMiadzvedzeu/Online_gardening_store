package org.shop.com.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.entity.CartEntity;
import org.shop.com.entity.CartItemEntity;
import org.shop.com.exceptions.CartItemInvalidArgumentException;
import org.shop.com.exceptions.CartItemNotFoundException;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.mapper.CartItemMapper;
import org.shop.com.repository.CartItemJpaRepository;
import org.shop.com.repository.CartJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemJpaRepository cartItemRepository;
    private final CartJpaRepository cartRepository;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartItemServiceImpl(CartItemJpaRepository cartItemRepository,
                               CartJpaRepository cartRepository, CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemMapper = cartItemMapper;
    }

    @Transactional
    @Override
    public CartItemDto addCartItem(CartItemCreateDto cartItemCreateDto, Long cartId) {
            log.debug("Attempting to add a cart item with product ID: {}, quantity: {} to cart ID: {}",
                cartItemCreateDto.getProductId(), cartItemCreateDto.getQuantity(), cartId);
        if (cartItemCreateDto.getProductId() == null || cartItemCreateDto.getQuantity() == null) {
            log.error("Failed to add cart item due to null product ID or quantity. Product ID: {}, Quantity: {}",
                    cartItemCreateDto.getProductId(), cartItemCreateDto.getQuantity());
            throw new CartItemInvalidArgumentException("Product ID and quantity must not be null.");
        }
        /** Загрузка CartEntity по cartId */
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("Cart not found with id: {}", cartId);
                    throw new CartNotFoundException("Cart not found with id: " + cartId);
                });

        CartItemEntity cartItem = cartItemMapper.fromCreateDto(cartItemCreateDto);
        cartItem.setCart(cart);
        cartItem = cartItemRepository.save(cartItem);

        log.debug("Cart item successfully added with ID: {}", cartItem.getId());
        return cartItemMapper.toDto(cartItem);
    }

    @Transactional
    @Override
    public void removeCartItem(Long cartItemId) {
        log.debug("Attempting to remove cart item with ID: {}", cartItemId);

        if (!cartItemRepository.existsById(cartItemId)) {
            log.error("Failed to remove cart item. CartItem with ID {} does not exist.", cartItemId);
            throw new CartItemNotFoundException("CartItem with ID " + cartItemId + " does not exist.");
        }

        cartItemRepository.deleteById(cartItemId);
        log.debug("Cart item successfully removed with ID: {}", cartItemId);
    }

    @Override
    public List<CartItemDto> getCartItemsByCartId(Long cartId) {
        log.debug("Retrieving cart items for cart ID: {}", cartId);

        if (!cartRepository.existsById(cartId)) {
            log.error("Cart with ID {} does not exist, cannot retrieve items.", cartId);
            throw new CartNotFoundException("Cart with ID " + cartId + " does not exist.");
        }

        List<CartItemDto> cartItems = cartItemRepository.findByCartId(cartId).stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Retrieved {} items for cart ID: {}", cartItems.size(), cartId);
        return cartItems;
    }

    @Transactional
    @Override
    public CartItemDto updateCartItemQuantity(Long cartItemId, Integer quantity) {
        log.debug("Updating quantity for cart item ID: {} to {}", cartItemId, quantity);

        if (quantity == null || quantity < 1) {
            log.error("Invalid quantity: {}. Quantity must be greater than 0 for cart item ID: {}", quantity, cartItemId);
            throw new CartItemInvalidArgumentException("Quantity must be greater than 0.");
        }

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> {
                    log.error("CartItem not found with ID: {}", cartItemId);
                    throw new CartItemNotFoundException("CartItem not found with ID: " + cartItemId);
                });

        cartItem.setQuantity(quantity);
        cartItem = cartItemRepository.save(cartItem);

        log.debug("Quantity for cart item ID: {} successfully updated to {}", cartItemId, quantity);
        return cartItemMapper.toDto(cartItem);
    }
}