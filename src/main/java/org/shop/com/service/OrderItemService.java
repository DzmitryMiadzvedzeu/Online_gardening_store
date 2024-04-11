package org.shop.com.service;

import org.shop.com.entity.OrderItemEntity;

public interface OrderItemService {

    OrderItemEntity prepareOrderItem(OrderItemEntity orderItem);
}