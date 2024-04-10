//package org.shop.com.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.shop.com.dto.*;
//import org.shop.com.entity.OrderItemEntity;
//import org.shop.com.exceptions.OrderItemAlreadyExistsException;
//import org.shop.com.exceptions.OrderItemNotFoundException;
//import org.shop.com.exceptions.OrderNotFoundException;
//import org.shop.com.mapper.OrderItemMapper;
//import org.shop.com.service.OrderItemService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/v1/order_items")
//@RequiredArgsConstructor
//public class OrderItemController {
//
//    private final OrderItemService orderItemService;
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<List<OrderItemDto>> getAll(@PathVariable long orderId) {
//        log.info("Fetching order items for order with id {}", orderId);
//        List<OrderItemDto> orderItems = orderItemService.findAllByOrderId(orderId);
//        return ResponseEntity.ok(orderItems);
//    }
//
//    @GetMapping("/{orderId}/{id}")
//    public ResponseEntity<OrderItemDto> findById(@PathVariable long orderId, @PathVariable long id) {
//        log.debug("Fetching order item with id {} for order with id {}", id, orderId);
//        OrderItemEntity orderItemEntity = orderItemService.findById(id);
//        return ResponseEntity.ok(OrderItemMapper.INSTANCE.toDto(orderItemEntity));
//    }
//
//    @PostMapping("/{orderId}")
//    public ResponseEntity<OrderItemDto> create(@PathVariable long orderId,
//                                               @RequestBody OrderItemCreateDto orderItemCreateDto) {
//        log.info("Adding item to order with id {}", orderId);
//        OrderItemEntity orderItemEntity = orderItemService.create(orderItemCreateDto, orderId);
//        OrderItemDto orderItemDto = OrderItemMapper.INSTANCE
//                .toDto(orderItemEntity);
//        log.debug("Order item successfully created with ID {}", orderItemDto.getId());
//        return new ResponseEntity<>(orderItemDto, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{orderId}")
//    public ResponseEntity<OrderItemDto> updateQuantity(@PathVariable long orderId,
//                                                       @RequestParam long id,
//                                                       @RequestParam Integer quantity) {
//        log.debug("Request received to update quantity of order item with id: " +
//                "{} from order with id {}", id, orderId);
//        OrderItemDto orderItemDto = orderItemService.updateQuantity(id, quantity);
//        log.debug("Quantity of order item updated successfully");
//        return ResponseEntity.ok(orderItemDto);
//    }
//
//    @DeleteMapping("/{orderId}")
//    public ResponseEntity<Void> delete(@PathVariable long orderId, @RequestParam long id) {
//        log.debug("Request received to delete order item with id {} from order with id {}", id, orderId);
//        orderItemService.delete(id);
//        log.debug("Order item with id {} deleted successfully from order with id {}", id, orderId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @ExceptionHandler(OrderNotFoundException.class)
//    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
//        log.error("Error: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(OrderItemNotFoundException.class)
//    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
//        log.error("Error: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(OrderItemAlreadyExistsException.class)
//    public ResponseEntity<String> handleOrderItemAlreadyExistsException(OrderItemAlreadyExistsException ex) {
//        log.error("Error: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
//    }
//}