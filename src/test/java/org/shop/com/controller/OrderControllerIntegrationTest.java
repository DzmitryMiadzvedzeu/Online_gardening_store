package org.shop.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.dto.OrderStatusDto;
import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.OrderItemEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.enums.OrderStatus;
import org.shop.com.mapper.OrderItemMapper;
import org.shop.com.mapper.OrderMapper;
import org.shop.com.security.JwtService;
import org.shop.com.service.HistoryService;
import org.shop.com.service.OrderItemService;
import org.shop.com.service.OrderService;
import org.shop.com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderMapper orderMapper;
    @MockBean
    private OrderItemMapper orderItemMapper;
    @MockBean
    private OrderItemService orderItemService;
    @MockBean
    private HistoryService historyService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void getAllOrders() throws Exception {
        when(orderService.getAll()).thenReturn(Arrays.asList(new OrderEntity(),
                new OrderEntity())); // Example
        mockMvc.perform(get("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(orderService).getAll();
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void getOrderStatus() throws Exception {
        when(orderService.getStatus(1L)).thenReturn(new OrderStatusDto
                (1L, OrderStatus.CREATED));
        mockMvc.perform(get("/v1/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CREATED"));
        verify(orderService).getStatus(1L);
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void getUserOrderHistory() throws Exception {
        when(historyService.get()).thenReturn(Arrays.asList(new HistoryEntity(),
                new HistoryEntity())); // Example
        mockMvc.perform(get("/v1/orders/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(historyService).get();
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/v1/orders/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(orderService).delete(1L);
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void addOrder() throws Exception {
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setItems(new ArrayList<>());
        OrderDto createdOrderDto = new OrderDto();
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        OrderEntity mockOrderEntity = new OrderEntity();
        mockOrderEntity.setUserEntity(mockUser);

        when(userService.getCurrentUserId()).thenReturn(1L);
        when(userService.findById(1L)).thenReturn(mockUser);
        when(orderMapper.orderCreateDtoToEntity(any(OrderCreateDto.class)))
                .thenReturn(mockOrderEntity);
        when(orderItemMapper.createDtoToEntity(any())).thenReturn(new OrderItemEntity());
        when(orderItemService.prepareOrderItem(any(OrderItemEntity.class)))
                .thenReturn(new OrderItemEntity());
        when(orderService.create(any(OrderEntity.class))).thenReturn(mockOrderEntity);
        when(orderMapper.toDto(any(OrderEntity.class))).thenReturn(createdOrderDto);

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        verify(orderService).create(any(OrderEntity.class));
    }
}