package org.shop.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.service.OrderItemService;
import org.shop.com.service.ProductService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private OrderItemEntity orderItemEntity;
    private ProductEntity productEntity;

    private long productId = 1L;
    private int quantity = 5;

    @BeforeEach
    void setUp() {
        orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProduct(new ProductEntity());
        orderItemEntity.getProduct().setId(productId);
        orderItemEntity.setQuantity(quantity);

        productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setPrice(new BigDecimal("19.99"));
    }

    @Test
    void prepareOrderItem_WhenProductExists_ShouldSetPriceAtPurchaseCorrectly() {
        // Подготовка
        when(productService.findById(productId)).thenReturn(productEntity);

        // Действие
        OrderItemEntity result = orderItemService.prepareOrderItem(orderItemEntity);

        // Проверка
        assertNotNull(result);
        assertEquals(new BigDecimal("99.95"), result.getPriceAtPurchase());
        verify(productService).findById(productId);
    }

    @Test
    void prepareOrderItem_WhenProductDoesNotExist_ShouldThrowException() {
        // Подготовка
        when(productService.findById(productId)).thenReturn(null);

        // Действие и проверка
        ProductNotFoundException thrown = assertThrows(
                ProductNotFoundException.class,
                () -> orderItemService.prepareOrderItem(orderItemEntity),
                "Ожидалось, что метод prepareOrderItem выбросит исключение, но этого не произошло"
        );

        assertTrue(thrown.getMessage().contains("Product not found with ID: " + productId));
        verify(productService).findById(productId);
    }
}