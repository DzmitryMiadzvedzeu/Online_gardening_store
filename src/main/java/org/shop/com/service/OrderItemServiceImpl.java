package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.OrderItemCreateDto;
import org.shop.com.dto.OrderItemDto;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.exceptions.OrderItemNotFoundException;
import org.shop.com.exceptions.OrderNotFoundException;
import org.shop.com.mapper.OrderItemMapper;
import org.shop.com.repository.OrderItemJpaRepository;
import org.shop.com.repository.OrderJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemJpaRepository orderItemRepository;

    private final OrderJpaRepository orderRepository;

    private final OrderItemMapper orderItemMapper;


    @Override
    public OrderItemEntity create(OrderItemCreateDto orderItemCreateDto, long orderId) {
        orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("Order with id {} not found", orderId);
            return new OrderNotFoundException("Order not found");
        });

        OrderItemEntity orderItemEntity = orderItemMapper.createDtoToEntity(orderItemCreateDto);
        orderItemEntity.setOrder(orderRepository.getReferenceById(orderId));

        return orderItemRepository.save(orderItemEntity);
    }



    @Override
    public List<OrderItemDto> findAllByOrderId(long orderId) {
        List<OrderItemEntity> orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto updateQuantity(long orderItemId, Integer quantity) {
        OrderItemEntity orderItemEntity = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> {
                    log.error("Order item with id {} not found", orderItemId);
                    return new OrderItemNotFoundException("Order item not found");
                });

        orderItemEntity.setQuantity(quantity);
        orderItemRepository.save(orderItemEntity);

        return orderItemMapper.toDto(orderItemEntity);
    }

    @Override
    public void delete(long orderId) {
        orderItemRepository.deleteAll(orderItemRepository.findByOrderId(orderId));
    }
}
