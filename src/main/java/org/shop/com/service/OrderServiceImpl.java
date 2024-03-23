package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.OrderNotFoundException;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.repository.OrderJpaRepository;
import org.shop.com.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderJpaRepository repository;
    private final UserJpaRepository userRepository;

    public OrderServiceImpl(OrderJpaRepository repository, UserJpaRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderEntity create(OrderEntity entity) {
        log.debug("Attempting to create order for user ID: {}", entity.getUserEntity().getId());
        // временный костыль до подключения спринг секьюрити в проект
        UserEntity userEntity = userRepository.findById(1L)
                .orElseThrow(() -> new UserNotFoundException("Default user not found"));
        entity.setUserEntity(userEntity);
        OrderEntity savedEntity = repository.save(entity);
        log.debug("Order created successfully with ID: {}", savedEntity.getId());
        return savedEntity;
    }

    @Override
    public List<OrderEntity> getAll() {
        log.debug("Fetching all orders");
        List<OrderEntity> orders = repository.findAll();
        log.debug("Found {} orders", orders.size());
        return orders;
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
        log.debug("Attempting to delete order with ID: {}", id);
        OrderEntity orderEntity = repository.findById(id).orElseThrow(() ->
                new OrderNotFoundException("Order with id " + id + " not found"));
        repository.deleteById(id);
        return orderEntity;
    }
}
