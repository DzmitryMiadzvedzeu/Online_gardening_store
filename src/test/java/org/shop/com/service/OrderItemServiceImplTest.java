package org.shop.com.service;

import org.junit.jupiter.api.BeforeEach;
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

    private OrderItemEntity orderItemEntity;
    private OrderItemCreateDto orderItemCreateDto;
    private long orderId = 1L;
    private long productId = 2L;
    private long orderItemId = 1L;

    @BeforeEach
    void setUp() {
        orderItemEntity = new OrderItemEntity();
        orderItemCreateDto = new OrderItemCreateDto(productId, 10, new BigDecimal("100"));
    }

    @Test
    void findAllByOrderId_WhenItemsExist_ShouldReturnNonEmptyListOfDtos() {
        // Arrange
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(Arrays.asList(orderItemEntity));

        // Act
        List<OrderItemDto> result = orderItemService.findAllByOrderId(orderId);

        // Assert
        assertFalse(result.isEmpty(), "Result should not be empty");
        verify(orderItemRepository).findByOrderId(orderId);
    }

    @Test
    void findById_WhenItemExists_ShouldReturnOrderItem() {
        // Arrange
        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItemEntity));

        // Act
        OrderItemEntity result = orderItemService.findById(orderItemId);

        // Assert
        assertNotNull(result, "The result should not be null");
        verify(orderItemRepository).findById(orderItemId);
    }

    @Test
    void create_WhenItemAlreadyExists_ShouldThrowOrderItemAlreadyExistsException() {
        // Arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new OrderEntity()));
        when(productService.findById(productId)).thenReturn(new ProductEntity());
        when(orderItemRepository.findByOrderIdAndProductId(orderId, productId)).thenReturn(Optional.of(orderItemEntity));

        // Act & Assert
        assertThrows(OrderItemAlreadyExistsException.class,
                () -> orderItemService.create(orderItemCreateDto, orderId),
                "Should throw OrderItemAlreadyExistsException when order item already exists");

        // Verify
        verify(orderRepository).findById(orderId);
        verify(productService).findById(productId);
        verify(orderItemRepository).findByOrderIdAndProductId(orderId, productId);
        verify(orderItemRepository, never()).save(any(OrderItemEntity.class));
    }

    @Test
    void updateQuantity_WhenItemExists_ShouldUpdateQuantityAndReturnUpdatedDto() {
        // Arrange
        Integer newQuantity = 5;
        orderItemEntity.setId(orderItemId);
        orderItemEntity.setQuantity(3);
        orderItemEntity.setPriceAtPurchase(new BigDecimal("100"));
        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItemEntity));
        when(orderItemRepository.save(any(OrderItemEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderItemDto result = orderItemService.updateQuantity(orderItemId, newQuantity);

        assertNotNull(result, "The result DTO should not be null");
        assertEquals(newQuantity, result.getQuantity(), "The quantity should be updated in the result DTO");
        verify(orderItemRepository).findById(orderItemId);
        verify(orderItemRepository).save(any(OrderItemEntity.class));
    }

    @Test
    void delete_WhenItemExists_ShouldCallDeleteMethod() {
        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItemEntity));

        orderItemService.delete(orderItemId);

        verify(orderItemRepository).findById(orderItemId);
        verify(orderItemRepository).delete(orderItemEntity);
    }

}