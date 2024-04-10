package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.entity.*;
import org.shop.com.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;



@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final ProductService productService;


    @Override
    public OrderItemEntity prepareOrderItem(OrderItemEntity orderItem) {
        ProductEntity product = productService.findById(orderItem.getProduct().getId());
        if (product == null) {
            throw new ProductNotFoundException("Product not found with ID: " + orderItem.getProduct().getId());
        }
        BigDecimal priceAtPurchase = product.getPrice().multiply(new BigDecimal(orderItem.getQuantity()));
        orderItem.setPriceAtPurchase(priceAtPurchase);
        return orderItem;
    }
}