package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.OrderNotFoundException;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.repository.OrderJpaRepository;
import org.shop.com.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository repository;

    private final UserJpaRepository userRepository;

    private final UserService userService;

    private final OrderItemService orderItemService;

    private final HistoryService historyService;

    @Override
    public OrderEntity create(OrderEntity entity) {
        Long currentUserId = userService.getCurrentUserId();
        UserEntity currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: "
                        + currentUserId));
        entity.setUserEntity(currentUser);

        OrderEntity createdOrder = repository.save(entity);

        if (entity.getOrderItems() != null) {
            for (OrderItemEntity item : entity.getOrderItems()) {
                item.setOrder(createdOrder);
                orderItemService.prepareOrderItem(item);
            }
        }

        historyService.add(createdOrder, currentUser);

        return createdOrder;
    }

    @Override
    public List<OrderEntity> getAll() {
        log.debug("Fetching all orders");
        List<OrderEntity> orders = repository.findAll();
        return orders;
    }

    @Override
    public OrderStatusDto getStatus(long id) {
        return repository.findById(id)
                .map(orderEntity -> new OrderStatusDto(orderEntity.getId(),
                        orderEntity.getStatus()))
                .orElseThrow(() -> new OrderNotFoundException("Order with id "
                        + id + " not found"));
    }

    @Override
    public OrderEntity delete(long id) {
        log.debug("Attempting to delete order with ID: {}", id);
        OrderEntity orderEntity = repository.findById(id).orElseThrow(() ->
                new OrderNotFoundException("Order with id " + id + " not found"));
        repository.deleteById(id);
        return orderEntity;
    }
}