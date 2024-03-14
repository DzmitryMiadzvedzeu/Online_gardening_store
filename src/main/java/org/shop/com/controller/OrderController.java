package org.shop.com.controller;

import lombok.RequiredArgsConstructor;
import org.shop.com.converter.OrderConverter;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderConverter<OrderEntity, OrderDto> converter;


    @GetMapping // Добавлен метод для обработки GET запросов
    public ResponseEntity<List<OrderDto>> listAllOrderDto(){
        List<OrderDto> orderDtos = orderService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderCreateDto orderCreateDto){
        OrderEntity orderEntity = converter.createDtoToEntity(orderCreateDto);
        OrderEntity createdOrderEntity = orderService.create(orderEntity);
        OrderDto createdOrderDto = converter.toDto(createdOrderEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDto);
    }
}