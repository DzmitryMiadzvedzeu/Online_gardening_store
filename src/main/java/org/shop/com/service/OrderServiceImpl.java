package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import org.shop.com.entity.OrderEntity;
import org.shop.com.repository.OrderJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderJpaRepository repository;

    @Override
    public OrderEntity create(OrderEntity entity) {
        return null;
    }
}
