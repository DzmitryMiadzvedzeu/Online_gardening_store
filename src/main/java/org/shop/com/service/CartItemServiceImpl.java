package org.shop.com.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.entity.CartEntity;
import org.shop.com.entity.CartItemEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.CartItemsInvalidArgumentException;
import org.shop.com.exceptions.CartItemsNotFoundException;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.mapper.CartItemMapper;
import org.shop.com.repository.CartItemJpaRepository;
import org.shop.com.repository.CartJpaRepository;
import org.shop.com.repository.ProductJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemJpaRepository cartItemRepository;
    private final CartJpaRepository cartRepository;
    private final ProductJpaRepository productJpaRepository;
    private final CartItemMapper cartItemMapper;

    @Transactional
    @Override
    public CartItemDto add(CartItemCreateDto cartItemCreateDto, Long cartId) {
        log.debug("Attempting to add a cart item with product ID: {}, quantity: {} to cart ID: {}",
                cartItemCreateDto.getProductId(), cartItemCreateDto.getQuantity(), cartId);

        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        ProductEntity product = productJpaRepository.findById(cartItemCreateDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + cartItemCreateDto.getProductId()));

        CartItemEntity cartItem = cartItemMapper.fromCreateDto(cartItemCreateDto);
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(cartItemCreateDto.getQuantity());
        cartItem = cartItemRepository.save(cartItem);

        log.debug("Cart item successfully added with ID: {}", cartItem.getId());
        return cartItemMapper.toDto(cartItem);
    }

    @Transactional
    @Override
    public void remove(Long cartItemId) {
        log.debug("Attempting to remove cart item with ID: {}", cartItemId);

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemsNotFoundException("Cart item not found with id: " + cartItemId));

        cartItemRepository.delete(cartItem);
        log.debug("Cart item successfully removed with ID: {}", cartItemId);
    }

    @Override
    public List<CartItemDto> getByCartId(Long cartId) {
        log.debug("Retrieving cart items for cart ID: {}", cartId);

        List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            log.error("No cart items found for cart ID: {}", cartId);
            throw new CartNotFoundException("No cart items found for cart ID: " + cartId);
        }

        return cartItems.stream().map(cartItemMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CartItemDto updateQuantity(Long cartItemId, Integer quantity) {
        log.debug("Updating quantity for cart item ID: {} to {}", cartItemId, quantity);

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemsNotFoundException("Cart item not found with id: " + cartItemId));

        if (quantity < 1) {
            log.error("Invalid quantity: {}. Quantity must be greater than 0 for cart item ID: {}", quantity, cartItemId);
            throw new CartItemsInvalidArgumentException("Quantity must be greater than 0, got " + quantity);
        }

        cartItem.setQuantity(quantity);
        cartItem = cartItemRepository.save(cartItem);
        log.debug("Quantity for cart item ID: {} successfully updated to {}", cartItemId, quantity);
        return cartItemMapper.toDto(cartItem);
    }
}