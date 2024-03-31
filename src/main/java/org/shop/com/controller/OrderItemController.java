package org.shop.com.controller;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.*;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.mapper.OrderItemMapper;
import org.shop.com.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/order_items")
@Slf4j
public class OrderItemController {

    private final OrderItemService orderItemService;


    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDto>> getAll(@PathVariable long orderId) {
        log.info("Fetching order items for order with id {}", orderId);
        List<OrderItemDto> orderItems = orderItemService.findAllByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/{orderId}/{id}")
    public ResponseEntity<OrderItemDto> findById (@PathVariable long orderId, @PathVariable long id) {
        log.debug("Fetching order item with id {} for order with id {}", id, orderId);
        OrderItemEntity orderItemEntity = orderItemService.findById(id);
        return ResponseEntity.ok(OrderItemMapper.INSTANCE.toDto(orderItemEntity));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<OrderItemDto> create(@PathVariable long orderId,
                                               @RequestBody OrderItemCreateDto orderItemCreateDto) {
        log.info("Adding item to order with id {}", orderId);
        OrderItemEntity orderItemEntity = orderItemService.create(orderItemCreateDto, orderId);
        OrderItemDto orderItemDto = OrderItemMapper.INSTANCE
                .toDto(orderItemEntity);
        log.debug("Order item successfully created with ID {}", orderItemDto.getId());
        return new ResponseEntity<>(orderItemDto, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderItemDto> updateQuantity(@PathVariable long orderId, @RequestParam long id, @RequestParam Integer quantity){
        log.debug("Request received to update quantity of order item with id: {} from order with id {}", id, orderId);
        OrderItemDto orderItemDto = orderItemService.updateQuantity(id, quantity);
        log.debug("Quantity of order item updated successfully");
        return ResponseEntity.ok(orderItemDto);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete (@PathVariable long orderId, @RequestParam long id) {
        log.debug("Request received to delete order item with id {} from order with id {}", id, orderId);
        orderItemService.delete(id);
        log.debug("Order item with id {} deleted successfully from order with id {}", id);
        return ResponseEntity.noContent().build();
    }

}