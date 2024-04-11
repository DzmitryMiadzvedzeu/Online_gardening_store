package org.shop.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.dto.HistoryDto;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.OrderNotFoundException;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.mapper.HistoryMapper;
import org.shop.com.mapper.OrderItemMapper;
import org.shop.com.mapper.OrderMapper;
import org.shop.com.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// http://localhost:8080/v1/orders
@Slf4j
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;
    private final OrderItemService orderItemService;

    private final HistoryService historyService;
    private final HistoryMapper historyMapper;

    private final UserService userService;

    @Operation(summary = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All orders retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto[].class)) })
    })
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll() {
        log.debug("Request received to list all orders");
        List<OrderDto> orderDtos = orderService.getAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Get order status by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderStatusDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusDto> getStatus(@PathVariable Long id) {
        log.debug("Request received to get the status of order with ID: {}", id);
        OrderStatusDto orderStatus = orderService.getStatus(id);
        return ResponseEntity.ok(orderStatus);
    }

    @Operation(summary = "Get user order history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User order history retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HistoryDto[].class)) })
    })
    @GetMapping("/history")
    public ResponseEntity<List<HistoryDto>> getUserOrderHistory() {
        List<HistoryEntity> userHistory = historyService.get();
        List<HistoryDto> historyDtos = userHistory.stream()
                .map(historyMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(historyDtos);
    }

    @Operation(summary = "Delete an order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        log.debug("Request received to delete order with ID: {}", id);
        orderService.delete(id);
        return ResponseEntity.noContent().build();

    }

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid order data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error due to a processing error")
    })
    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderCreateDto orderCreateDto) {
        Long currentUserId = userService.getCurrentUserId();
        UserEntity currentUser = userService.findById(currentUserId);
        if (currentUser == null) {
            throw new UserNotFoundException("User with ID: " + currentUserId + " not found");
        }
        OrderEntity orderEntity = orderMapper.orderCreateDtoToEntity(orderCreateDto);
        orderEntity.setUserEntity(currentUser);

        List<OrderItemEntity> orderItems = orderCreateDto.getItems().stream()
                .map(orderItemMapper::createDtoToEntity)
                .map(orderItemService::prepareOrderItem)
                .collect(Collectors.toList());

        orderItems.forEach(item -> item.setOrder(orderEntity));
        orderEntity.setOrderItems(orderItems);

        OrderEntity createdOrder = orderService.create(orderEntity);

        OrderDto createdOrderDto = orderMapper.toDto(createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDto);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        log.error("Order not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}