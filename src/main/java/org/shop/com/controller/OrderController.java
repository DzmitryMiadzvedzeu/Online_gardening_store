package org.shop.com.controller;

import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "/v1/orders")
public class OrderController {

    @PostMapping
    public OrderDto addOrder(@RequestBody OrderCreateDto orderDto){

    }
}
