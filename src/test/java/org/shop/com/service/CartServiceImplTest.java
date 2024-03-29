package org.shop.com.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.entity.CartEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.repository.CartJpaRepository;
import org.shop.com.repository.UserJpaRepository;
import org.shop.com.mapper.CartMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartJpaRepository cartRepository;
    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private CartServiceImpl cartService;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /** Создание новой корзины для пользователя, у которого её ещё нет.*/
    @Test
    public void createOrUpdateCart_NewCart_Success() {
        Long userId = 1L;
        CartCreateDto createDto = new CartCreateDto(userId, Collections.emptyList());
        when(userService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(cartMapper.toDto(any(CartEntity.class))).thenReturn(new CartDto());

        CartDto result = cartService.createOrUpdate(createDto);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    /**Обновление существующей корзины пользователя.*/
    @Test
    public void createOrUpdateCart_ExistingCart_Success() {
        Long userId = 1L;
        CartCreateDto createDto = new CartCreateDto(userId, Collections.emptyList());
        UserEntity userEntity = new UserEntity();
        CartEntity existingCart = new CartEntity();
        existingCart.setUser(userEntity);
        existingCart.setItems(Collections.emptyList());

        when(userService.getCurrentUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(cartMapper.toDto(any(CartEntity.class))).thenReturn(new CartDto());

        CartDto result = cartService.createOrUpdate(createDto);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(existingCart);
    }

    /** возвращает информацию о корзине пользователя по его ID */
    @Test
    public void getCartByUserId_CartExists_Success() {
        Long userId = 1L;
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(new CartEntity()));
        when(cartMapper.toDto(any(CartEntity.class))).thenReturn(new CartDto());

        CartDto result = cartService.getByUserId(userId);

        // Проверки
        assertNotNull(result);
    }
    @Test(expected = CartNotFoundException.class)
    public void getCartByUserId_CartDoesNotExist_ThrowException() {
        Long userId = 1L;
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Вызов метода и ожидаемое исключение
        cartService.getByUserId(userId);
    }

    /**  удаляет корзину пользователя*/
    @Test
    public void deleteCart_CartExists_Success() {
        Long userId = 1L;
        when(cartRepository.existsById(userId)).thenReturn(true);

        cartService.delete(userId);

        verify(cartRepository, times(1)).deleteById(userId);
    }

    /** возвращает список всех корзин*/
    @Test
    public void getAllCarts_ReturnsListOfCarts() {
        when(cartRepository.findAll()).thenReturn(Collections.singletonList(new CartEntity()));
        when(cartMapper.toDto(any(CartEntity.class))).thenReturn(new CartDto());

        List<CartDto> result = cartService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
    /**Проверка удаления корзины, когда она не существует*/
    @Test(expected = CartNotFoundException.class)
    public void deleteCart_NotExisting_ThrowsException() {
        Long userId = 2L;
        when(cartRepository.existsById(userId)).thenReturn(false);

        cartService.delete(userId);
    }
    @Test
    public void getAllCarts_ReturnsAllCarts() {
        List<CartEntity> cartEntities = Arrays.asList(new CartEntity(), new CartEntity());
        when(cartRepository.findAll()).thenReturn(cartEntities);
        when(cartMapper.toDto(any(CartEntity.class))).thenReturn(new CartDto());

        List<CartDto> result = cartService.getAll();

        assertNotNull(result);
        assertEquals(cartEntities.size(), result.size());
    }


}