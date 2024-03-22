package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.exceptions.OrderNotFoundException;
import org.shop.com.repository.OrderJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderJpaRepository repository;

    public OrderServiceImpl(OrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrderEntity create(OrderEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<OrderEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public OrderStatusDto getOrderStatusById(long id) {
        return repository.findById(id)
                .map(orderEntity -> new OrderStatusDto(orderEntity.getId(),
                        orderEntity.getStatus()))
                .orElseThrow(() -> new OrderNotFoundException("Order with id "
                        + id + " not found"));
    }

    @Override
    public OrderEntity deleteOrderEntityById(long id) {
        OrderEntity orderEntity = repository.findById(id).orElseThrow(() ->
                new OrderNotFoundException("Order with id " + id + " not found"));
        repository.deleteById(id);
        return orderEntity;
    }
}
