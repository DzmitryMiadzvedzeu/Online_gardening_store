package org.shop.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.entity.CartEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.mapper.CartItemMapper;
import org.shop.com.mapper.CartMapper;
import org.shop.com.repository.CartJpaRepository;
import org.shop.com.repository.ProductJpaRepository;
import org.shop.com.repository.UserJpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartJpaRepository cartRepository;
    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private ProductJpaRepository productJpaRepository;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private final Long userId = 1L;
    private UserEntity user;
    private CartEntity cart;
    private CartDto cartDto;
    private CartCreateDto createDto;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(userId);
        createDto = new CartCreateDto();
        createDto.setUserId(userId);
        createDto.setItems(Collections.emptyList());

        cart = new CartEntity();
        cart.setUser(user);

        cartDto = new CartDto();
        cartDto.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        /** корзина пользователя не существует и будет создана новая */
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        /** Конфигурация для нового CartEntity при сохранении */
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(i -> i.getArgument(0));

        when(cartMapper.toDto(any(CartEntity.class))).thenReturn(new CartDto());
    }

    @Test
    void createOrUpdateCart_Success() {
        CartDto result = cartService.createOrUpdateCart(createDto);

        assertNotNull(result, "Result should not be null.");
        verify(userRepository).findById(userId);
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(any(CartEntity.class));
    }


    @Test
    void getCartByUserId_Success() {
        Long userId = user.getId();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartMapper.toDto(Mockito.<CartEntity>any())).thenReturn(cartDto);
        CartDto result = cartService.getCartByUserId(userId);

        assertNotNull(result);
        verify(cartRepository).findByUserId(userId);
    }

    @Test
    void getCartByUserId_NotFound() {
        Long userId = user.getId();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getCartByUserId(userId));
    }

    @Test
    void deleteCart_Success() {
        Long userId = user.getId();

        when(cartRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> cartService.deleteCart(userId));
        verify(cartRepository).deleteById(userId);
    }

    @Test
    void deleteCart_NotFound() {
        Long userId = user.getId();

        when(cartRepository.existsById(userId)).thenReturn(false);

        assertThrows(CartNotFoundException.class, () -> cartService.deleteCart(userId));
    }

    @Test
    void getAllCarts_Success() {
        when(cartRepository.findAll()).thenReturn(List.of(cart));
        when(cartMapper.toDto(Mockito.<CartEntity>any())).thenReturn(cartDto);

        List<CartDto> result = cartService.getAllCarts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}