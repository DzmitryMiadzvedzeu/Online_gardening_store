package org.shop.com.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.enums.OrderStatus;
import org.shop.com.mapper.OrderMapper;
import org.shop.com.service.OrderService;
import org.shop.com.service.UserService;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;
    @Mock
    private OrderService orderService;
    @Mock
    private UserService userService;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }


    @Test
    public void listAllOrderDto_ShouldReturnOrders() throws Exception {
        OrderDto orderDto1 = new OrderDto(1L, "Address 1",
                "Courier", "1234567890", OrderStatus.CREATED,
                null, null);
        OrderDto orderDto2 = new OrderDto(2L, "Address 2",
                "Courier", "0987654321", OrderStatus.DELIVERED,
                null, null);
        List<OrderDto> expectedDtos = Arrays.asList(orderDto1, orderDto2);

        when(orderService.getAll()).thenReturn(Arrays.asList(new OrderEntity(), new OrderEntity()));
        when(orderMapper.toDto(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity entity = invocation.getArgument(0);
            // примерно какой DTO возвращать, основываясь на порядке вызова
            int index = (int) (Math.random() * expectedDtos.size());
            return expectedDtos.get(index);
        });

        mockMvc.perform(get("/v1/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    public void getOrderStatus_ShouldReturnOrderStatus() throws Exception {
        Long orderId = 1L;
        OrderStatusDto orderStatusDto = new OrderStatusDto(orderId, OrderStatus.CREATED);

        when(orderService.getOrderStatusById(orderId)).thenReturn(orderStatusDto);

        mockMvc.perform(get("/v1/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value(OrderStatus.CREATED.name()));
    }

    @Test
    void addOrder_ShouldCreateOrderAndReturnCreatedOrderDto() throws Exception {
        OrderCreateDto orderCreateDto = new OrderCreateDto("Delivery Address",
                "Courier", "1234567890");
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
        orderEntity.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
        orderEntity.setContactPhone(orderCreateDto.getContactPhone());
        UserEntity currentUser = new UserEntity();
        currentUser.setId(1L);

        OrderDto createdOrderDto = new OrderDto();
        createdOrderDto.setId(1L);
        createdOrderDto.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
        createdOrderDto.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
        createdOrderDto.setContactPhone(orderCreateDto.getContactPhone());

        when(userService.getCurrentUserId()).thenReturn(currentUser.getId());
        when(userService.findById(anyLong())).thenReturn(currentUser);
        when(orderMapper.orderCreateDtoToEntity(any(OrderCreateDto.class))).thenReturn(orderEntity);
        when(orderService.create(any(OrderEntity.class))).thenReturn(orderEntity);
        when(orderMapper.toDto(any(OrderEntity.class))).thenReturn(createdOrderDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.deliveryAddress", Matchers.is(createdOrderDto.getDeliveryAddress())))
                .andExpect(jsonPath("$.deliveryMethod", Matchers.is(createdOrderDto.getDeliveryMethod())))
                .andExpect(jsonPath("$.contactPhone", Matchers.is(createdOrderDto.getContactPhone())));

        verify(userService, times(1)).getCurrentUserId();
        verify(userService, times(1)).findById(anyLong());
        verify(orderService, times(1)).create(any(OrderEntity.class));
        verify(orderMapper, times(1)).orderCreateDtoToEntity(any(OrderCreateDto.class));
        verify(orderMapper, times(1)).toDto(any(OrderEntity.class));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteOrder_ShouldDeleteOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}