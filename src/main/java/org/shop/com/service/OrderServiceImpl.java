package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import org.shop.com.dto.OrderDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.repository.OrderJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderJpaRepository repository;

    @Override
    public OrderEntity create(OrderEntity entity) {
        //
        return repository.save(entity);
    }

    @Override
    public List<OrderEntity> getAll() {
        return repository.findAll();
    }


}
