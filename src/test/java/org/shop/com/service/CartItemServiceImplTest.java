package org.shop.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.entity.CartEntity;
import org.shop.com.entity.CartItemEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.CartItemsInvalidArgumentException;
import org.shop.com.exceptions.CartItemsNotFoundException;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.mapper.CartItemMapper;
import org.shop.com.repository.CartItemJpaRepository;
import org.shop.com.repository.CartJpaRepository;
import org.shop.com.repository.ProductJpaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CartItemServiceImplTest {

    @Mock
    private CartItemJpaRepository cartItemRepository;
    @Mock
    private CartJpaRepository cartRepository;
    @Mock
    private ProductJpaRepository productJpaRepository;
    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    private final Long cartId = 1L;
    private final Long productId = 1L;
    private final Long cartItemId = 1L;
    private CartEntity cart;
    private ProductEntity product;
    private CartItemEntity cartItem;
    private CartItemCreateDto cartItemCreateDto;
    private CartItemDto cartItemDto;

    @BeforeEach
    void setUp() {
        cart = new CartEntity();
        product = new ProductEntity();
        product.setId(productId);
        cartItem = new CartItemEntity();
        cartItem.setId(cartItemId);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        cartItemCreateDto = new CartItemCreateDto(productId, 1);
        cartItemDto = new CartItemDto(cartItemId, productId, 1);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemMapper.fromCreateDto(any(CartItemCreateDto.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(any(CartItemEntity.class))).thenReturn(cartItemDto);
        when(cartItemRepository.save(any(CartItemEntity.class))).thenReturn(cartItem);
    }

    @Test
    void addCartItem_Success() {
        CartItemDto result = cartItemService.add(cartItemCreateDto, cartId);

        assertNotNull(result);
        assertEquals(cartItemId, result.getId());
        verify(cartRepository).findById(cartId);
        verify(productJpaRepository).findById(productId);
        verify(cartItemRepository).save(any(CartItemEntity.class));
    }

    @Test
    void removeCartItem_Success() {
        when(cartItemRepository.existsById(cartItemId)).thenReturn(true);

        assertDoesNotThrow(() -> cartItemService.remove(cartItemId));
        verify(cartItemRepository).deleteById(cartItemId);
    }

    @Test
    void removeCartItem_NotFound() {
        when(cartItemRepository.existsById(cartItemId)).thenReturn(false);

        assertThrows(CartItemsNotFoundException.class, () -> cartItemService.remove(cartItemId));
    }

    @Test
    void getCartItemsByCartId_Success() {

        when(cartRepository.existsById(cartId)).thenReturn(true);

        when(cartItemRepository.findByCartId(cartId)).thenReturn(List.of(cartItem));


        List<CartItemDto> result = cartItemService.getByCartId(cartId);


        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(cartItemRepository).findByCartId(cartId);
    }

    @Test
    void updateCartItemQuantity_Success() {
        Integer newQuantity = 2;

        /** Настройка мока для возвращения существующего CartItemEntity */
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));


        when(cartItemMapper.toDto(any(CartItemEntity.class))).thenAnswer(invocation -> {
            CartItemEntity entity = invocation.getArgument(0);
            return new CartItemDto(entity.getId(), entity.getProduct().getId(), entity.getQuantity());
        });

        // Выполнение действия, которое требуется протестировать
        CartItemDto result = cartItemService.updateQuantity(cartItemId, newQuantity);


        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity(), "The quantity in the result should match the new quantity.");
        verify(cartItemRepository).findById(cartItemId);
        verify(cartItemRepository).save(any(CartItemEntity.class));
    }

    @Test
    void updateCartItemQuantity_NotFound() {
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        assertThrows(CartItemsNotFoundException.class, () -> cartItemService.updateQuantity(cartItemId, 2));
    }

    @Test
    void getCartItemsByCartId_CartNotFound() {
        when(cartRepository.existsById(cartId)).thenReturn(false);

        assertThrows(CartNotFoundException.class, () -> cartItemService.getByCartId(cartId));
    }

    @Test
    void addCartItem_ProductNotFound() {
        when(productJpaRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartItemService.add(cartItemCreateDto, cartId));
    }

    @Test
    void addCartItem_CartNotFound() {
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartItemService.add(cartItemCreateDto, cartId));
    }

    /**
     * Метод для демонстрации проверки невалидного количества при обновлении элемента корзины.
     */
    @Test
    void updateCartItemQuantity_InvalidQuantity() {
        Integer invalidQuantity = 0;
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        assertThrows(CartItemsInvalidArgumentException.class, () -> cartItemService.updateQuantity(cartItemId, invalidQuantity));
    }
}