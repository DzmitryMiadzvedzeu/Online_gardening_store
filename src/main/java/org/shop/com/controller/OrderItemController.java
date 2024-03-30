package org.shop.com.controller;

import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.OrderItemCreateDto;
import org.shop.com.dto.OrderItemDto;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orderitems")
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

    @PostMapping("/{orderId}")
    public ResponseEntity<OrderItemEntity> addItemToOrder(@PathVariable long orderId,
                                                          @RequestBody OrderItemCreateDto orderItemCreateDto) {
        log.info("Adding item to order with id {}", orderId);
        OrderItemEntity addedItem = orderItemService.create(orderItemCreateDto, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
    }

}