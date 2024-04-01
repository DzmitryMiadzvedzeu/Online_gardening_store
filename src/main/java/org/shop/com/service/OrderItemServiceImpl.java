package org.shop.com.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.OrderItemCreateDto;
import org.shop.com.dto.OrderItemDto;
import org.shop.com.entity.*;
import org.shop.com.exceptions.*;
import org.shop.com.mapper.OrderItemMapper;
import org.shop.com.repository.OrderItemJpaRepository;
import org.shop.com.repository.OrderJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemJpaRepository orderItemRepository;

    private final OrderJpaRepository orderRepository;

    private final ProductService productService;

    @Override
    public List<OrderItemDto> findAllByOrderId(long orderId) {
        log.info("Fetching all order items for order with id {}", orderId);
        List<OrderItemEntity> orderItems = orderItemRepository.findByOrderId(orderId);

        if (orderItems.isEmpty()) {
            log.error("No order items found for order with id {}", orderId);
        } else {
            log.debug("Successfully found {} order items for order with id {}", orderItems.size(), orderId);
        }

        return orderItems.stream()
                .map(OrderItemMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemEntity findById(long id) {
        log.debug("Obtaining order item with id {}", id);
        return orderItemRepository.findById(id).orElseThrow(() -> {
            log.error("Order item with id {} not found", id);
               return new OrderItemNotFoundException("Can't find order item with id" +id);
                });
    }

    @Transactional
    @Override
    public OrderItemEntity create(OrderItemCreateDto orderItemCreateDto, long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> {
            log.error("Order with id {} not found", orderId);
            return new OrderNotFoundException("Order not found");
        });

        ProductEntity productById = productService.findById(orderItemCreateDto.getProductId());

        Optional<OrderItemEntity> existingOrderItem = orderItemRepository.findByOrderIdAndProductId(orderId, orderItemCreateDto.getProductId());

        if (existingOrderItem.isPresent()) {
            log.error("OrderItem with productId {} in order {} already exists", orderItemCreateDto.getProductId(), orderId);
            throw new OrderItemAlreadyExistsException("OrderItem with productId " + orderItemCreateDto.getProductId() + " already exists in order " + orderId);
        } else {
            OrderItemEntity created = OrderItemMapper.INSTANCE.createDtoToEntity(orderItemCreateDto);
            created.setOrder(order);
            created.setProduct(productById);
            OrderItemEntity saved = orderItemRepository.save(created);
            log.debug("Order item created successfully with ID: {}", saved.getId());
            return saved;
        }
    }

    @Transactional
    @Override
    public OrderItemDto updateQuantity(long orderItemId, Integer quantity) {
        OrderItemEntity orderItemEntity = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> {
                    log.error("Order item with id {} not found", orderItemId);
                    return new OrderItemNotFoundException("Order item not found");
                });

        orderItemEntity.setQuantity(quantity);
        orderItemRepository.save(orderItemEntity);

        return OrderItemMapper.INSTANCE.toDto(orderItemEntity);
    }

    @Transactional
    @Override
    public void delete(long id) {
        log.info("Attempting to delete order item with id {}", id);
        OrderItemEntity deletedOrderItem = findById(id);
        orderItemRepository.delete(deletedOrderItem);
        log.debug("Order item with id {} deleted successfully", id);
    }

}
