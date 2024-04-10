package org.shop.com.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.OrderNotFoundException;
import org.shop.com.repository.OrderJpaRepository;
import org.shop.com.repository.UserJpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderJpaRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private HistoryService historyService;
    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private OrderServiceImpl service;

    @Test
    public void whenCreateOrder_thenOrderMustBeSavedAndHistoryAdded() {
        // сделаю пока что ложного пользователя, до добавления аутентиффикации
        Long fakeUserId = 1L;
        UserEntity fakeUser = new UserEntity();
        fakeUser.setId(fakeUserId);
        OrderEntity order = new OrderEntity("SomeAddress", "SomeMethod", "+123456789");
        order.setUserEntity(fakeUser);

        when(userService.getCurrentUserId()).thenReturn(fakeUserId);
        when(userRepository.findById(fakeUserId)).thenReturn(Optional.of(fakeUser));
        when(repository.save(any(OrderEntity.class))).thenReturn(order);

        OrderEntity savedOrder = service.create(order);

        assertNotNull(savedOrder);
        verify(userService, times(1)).getCurrentUserId();
        verify(userRepository, times(1)).findById(fakeUserId);
        verify(repository, times(1)).save(order);
        verify(historyService, times(1)).add(order, fakeUser);
    }

    @Test
    public void whenFindAllOrder_thenAllOrdersCanSee() {
        OrderEntity order1 = new OrderEntity("Address1",
                "Method1", "+1234567890");
        OrderEntity order2 = new OrderEntity("Address2",
                "Method2", "+0987654321");
        List<OrderEntity> expectedOrders = Arrays.asList(order1, order2);
        when(repository.findAll()).thenReturn(expectedOrders);
        List<OrderEntity> actualOrders = service.getAll();
        assertEquals(expectedOrders, actualOrders,
                "The returned orders should match the expected");
        verify(repository, times(1)).findAll();
    }

    @Test
    public void whenDeleteOrderEntityById_thenOrderShouldBeDeleted() {
        long orderId = 1L;
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(repository).deleteById(orderId);
        OrderEntity deletedOrder = service.delete(orderId);
        assertNotNull(deletedOrder);
        verify(repository, times(1)).deleteById(orderId);
    }

    @Test
    public void whenDeleteOrderEntityById_thenThrowOrderNotFoundException() {
        long orderId = 1L;
        when(repository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,
                () -> service.delete(orderId));
    }
}