package org.shop.com.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.OrderItemCreateDto;
import org.shop.com.dto.OrderItemDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.OrderItemAlreadyExistsException;
import org.shop.com.repository.OrderItemJpaRepository;
import org.shop.com.repository.OrderJpaRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private OrderItemJpaRepository orderItemRepository;

    @Mock
    private OrderJpaRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;


    @Test
    void findAllByOrderIdTest() {
        long orderId = 1L;
        OrderItemEntity orderItemEntity = new OrderItemEntity();

        List<OrderItemEntity> mockOrderItems = Arrays.asList(orderItemEntity);

        when(orderItemRepository.findByOrderId(orderId)).thenReturn(mockOrderItems);

        List<OrderItemDto> result = orderItemService.findAllByOrderId(orderId);

        assertFalse(result.isEmpty());
        verify(orderItemRepository).findByOrderId(orderId);
    }

    @Test
    void findById() {
        long id = 1L;
        OrderItemEntity orderItemEntity = new OrderItemEntity();

        when(orderItemRepository.findById(id)).thenReturn(Optional.of(orderItemEntity));

        OrderItemEntity result = orderItemService.findById(id);

        assertNotNull(result, "The result should not be null");
        verify(orderItemRepository).findById(id);
    }

    @Test
    void createThrowsOrderItemAlreadyExistsException() {
        long orderId = 1L;
        long productId = 2L;
        OrderItemCreateDto orderItemCreateDto = new OrderItemCreateDto(productId, 10, new BigDecimal("100"));
        OrderEntity order = new OrderEntity();
        ProductEntity product = new ProductEntity();
        OrderItemEntity existingOrderItem = new OrderItemEntity();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productService.findById(productId)).thenReturn(product);
        when(orderItemRepository.findByOrderIdAndProductId(orderId, productId)).thenReturn(Optional.of(existingOrderItem));

        assertThrows(OrderItemAlreadyExistsException.class, () -> orderItemService.create(orderItemCreateDto, orderId));

        verify(orderRepository).findById(orderId);
        verify(productService).findById(productId);
        verify(orderItemRepository).findByOrderIdAndProductId(orderId, productId);
        verify(orderItemRepository, never()).save(any(OrderItemEntity.class));
    }

    @Test
    void updateQuantity() {
    }

    @Test
    void delete() {
    }
}