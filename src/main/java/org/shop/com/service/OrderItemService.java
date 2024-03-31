package org.shop.com.service;
import org.shop.com.dto.OrderItemCreateDto;
import org.shop.com.dto.OrderItemDto;
import org.shop.com.entity.OrderItemEntity;

import java.util.List;

public interface OrderItemService {

    List<OrderItemDto> findAllByOrderId(long orderId);

    OrderItemEntity findById(long orderItemId);

    OrderItemEntity create(OrderItemCreateDto orderItemCreateDto, long orderId);

    OrderItemDto updateQuantity(long orderItemId, Integer quantity);

    void delete(long orderId);

}
