package org.shop.com.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.converter.OrderConverter;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.mapper.OrderMapper;
import org.shop.com.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/orders")
//@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
//    private final OrderConverter<OrderEntity, OrderDto> converter;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> listAllOrderDto(){
        log.debug("Request received to list all orders");
        List<OrderDto> orderDtos = orderService.getAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Returning {} orders", orderDtos.size());
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusDto> getOrderStatus(@PathVariable Long id){
        log.debug("Request received to get the status of order with ID: {}", id);
        OrderStatusDto orderStatus = orderService.getOrderStatusById(id);
        log.debug("Returning status for order ID {}: {}", id, orderStatus.getStatus());
        return ResponseEntity.ok(orderStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable long id){
        log.debug("Request received to delete order with ID: {}", id);
        orderService.deleteOrderEntityById(id);
        log.debug("Order with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();

    }

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderCreateDto orderCreateDto){
        log.debug("Creating new order with delivery address: {}", orderCreateDto.getDeliveryAddress());
        log.debug("Creating new order with delivery address: {}", orderCreateDto.getDeliveryMethod());
        log.debug("Creating new order with delivery address: {}", orderCreateDto.getContactPhone());

        OrderEntity orderEntity = orderMapper.orderCreateDtoToEntity(orderCreateDto);
        OrderEntity createdOrderEntity = orderService.create(orderEntity);
        OrderDto createdOrderDto = orderMapper.toDto(createdOrderEntity);

        log.debug("Order created successfully with ID: {}", createdOrderDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDto);
    }
}