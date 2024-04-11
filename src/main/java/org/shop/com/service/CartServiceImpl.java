package org.shop.com.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.entity.CartEntity;
import org.shop.com.entity.CartItemEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.CartInvalidArgumentException;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.mapper.CartItemMapper;
import org.shop.com.mapper.CartMapper;
import org.shop.com.repository.CartItemJpaRepository;
import org.shop.com.repository.CartJpaRepository;
import org.shop.com.repository.ProductJpaRepository;
import org.shop.com.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartJpaRepository cartRepository;
    private final UserJpaRepository userRepository;
    private final ProductJpaRepository productRepository;
    private final CartMapper cartMapper;
    private final CartItemJpaRepository cartItemRepository;
    private final UserService userService;

    @Transactional
    @Override
    public CartDto createOrUpdate(CartCreateDto createDto) {
        log.debug("Creating or updating cart for current user");

        Long userId = userService.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    return newCart;
                });

        updateCartWithItems(cart, createDto.getItems());
        cart = cartRepository.save(cart);
        log.debug("Cart for user ID: {} created or updated successfully", userId);
        return cartMapper.toDto(cart);
    }
    private void updateCartWithItems(CartEntity cart, List<CartItemCreateDto> itemsDto) {
        if (cart.getId() == null) {
            cartRepository.save(cart);
        }

        List<CartItemEntity> cartItems = itemsDto.stream().map(itemDto -> {
            ProductEntity product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));

            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setProduct(product);
            cartItem.setQuantity(itemDto.getQuantity());
            cartItem.setCart(cart);
            return cartItem;
        }).collect(Collectors.toList());

        cartItemRepository.saveAll(cartItems);  // Сохраняем все элементы корзины
        cart.setItems(cartItems);  // Обновляем список элементов в объекте корзины
        cartRepository.save(cart);  // Сохраняем изменения в корзине
    }

    @Override
    public CartDto getByUserId(Long userId) {
        log.debug("Retrieving cart for user ID: {}", userId);
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));
        return cartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public void delete(Long userId, Long productId) {
        log.debug("Attempting to remove product with ID: {} from cart for user ID: {}", productId, userId);
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart with user ID " + userId + " does not exist"));

        List<CartItemEntity> itemsToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .collect(Collectors.toList());

        if (itemsToRemove.isEmpty()) {
            log.warn("No product with ID: {} found in the cart for user ID: {}", productId, userId);
            throw new RuntimeException("Product with ID: " + productId + " not found in cart");
        }

        // Удаление товаров из корзины
        cartItemRepository.deleteAll(itemsToRemove);
        cart.getItems().removeAll(itemsToRemove);
        log.debug("Product with ID: {} removed from the cart for user ID: {}", productId, userId);

        // Проверка, остались ли еще товары в корзине
        if (cart.getItems().isEmpty()) {
            cartRepository.delete(cart); // Удаляем корзину, если это был последний товар
            log.debug("Cart for user ID: {} deleted as it was empty after removing products", userId);
        } else {
            cartRepository.save(cart); // Сохраняем изменения в корзине, если в ней остались товары
            log.debug("Cart for user ID: {} updated with remaining products", userId);
        }
    }

    @Override
    public List<CartDto> getAll() {
        log.debug("Retrieving all carts");
        List<CartEntity> carts = cartRepository.findAll();
        return carts.stream().map(cartMapper::toDto).collect(Collectors.toList());
    }
}