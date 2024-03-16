package org.shop.com.controller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.shop.com.converter.OrderConverter;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.enums.OrderStatus;
import org.shop.com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderConverter<OrderEntity, OrderDto> converter;

    @Test
    public void listAllOrderDto_ShouldReturnOrders() throws Exception {
        OrderDto orderDto1 = new OrderDto(1L, "Address 1", "Courier",
                "1234567890", OrderStatus.CREATED, new Date(), new Date());
        OrderDto orderDto2 = new OrderDto(2L, "Address 2", "Courier",
                "0987654321", OrderStatus.CREATED, new Date(), new Date());
        List<OrderDto> orderDtos = Arrays.asList(orderDto1, orderDto2);
        when(orderService.getAll()).thenReturn(Arrays.asList(new OrderEntity(),
                new OrderEntity()));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(orderDtos.size()));
    }

    @Test
    public void getOrderStatus_ShouldReturnStatus() throws Exception {
        OrderStatusDto orderStatusDto = new OrderStatusDto(1L, OrderStatus.CREATED);
        when(orderService.getOrderStatusById(1L)).thenReturn(orderStatusDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(orderStatusDto.getStatus()
                        .toString()));
    }

    @Test
    public void addOrder_ShouldCreateOrder() throws Exception {
        OrderCreateDto orderCreateDto = new OrderCreateDto("New Address",
                "Courier",
                "1234567890", OrderStatus.CREATED, new Date(), new Date());
        OrderDto createdOrderDto = new OrderDto(1L, "New Address",
                "Courier",
                "1234567890", OrderStatus.CREATED, new Date(), new Date());
        when(orderService.create(any(OrderEntity.class))).thenReturn(new OrderEntity());
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"deliveryAddress\": \"New Address\", \"deliveryMethod\": " +
                                "\"Courier\", \"contactPhone\": \"1234567890\" }"))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteOrder_ShouldDeleteOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}