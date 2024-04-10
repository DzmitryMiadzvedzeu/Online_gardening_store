//package org.shop.com.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.shop.com.dto.OrderItemCreateDto;
//import org.shop.com.dto.OrderItemDto;
//import org.shop.com.entity.OrderItemEntity;
//import org.shop.com.entity.ProductEntity;
//import org.shop.com.mapper.OrderItemMapper;
//import org.shop.com.service.OrderItemService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(OrderItemController.class)
//public class OrderItemControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private OrderItemService orderItemService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//    private OrderItemDto orderItemDto;
//    private OrderItemCreateDto createDto;
//    private OrderItemEntity orderItemEntity;
//    private long orderId = 1L;
//    private long orderItemId = 1L;
//
//    @BeforeEach
//    void setUp() {
//        createDto = new OrderItemCreateDto(2L, 10, new BigDecimal("100.00"));
//        orderItemDto = new OrderItemDto(orderItemId, 2L, 10, new BigDecimal("100.00"));
//        orderItemEntity = OrderItemMapper.INSTANCE.toEntity(orderItemDto);
//
//        ProductEntity productEntity = new ProductEntity();
//        productEntity.setId(2L);
//        orderItemEntity.setProduct(productEntity);
//        orderItemEntity.setQuantity(10);
//        orderItemEntity.setPriceAtPurchase(new BigDecimal("100.00"));
//    }
//
//    @Test
//    @DisplayName("GET /v1/order_items/{orderId} - Success")
//    void getAll_ShouldReturnOrderItems() throws Exception {
//        given(orderItemService.findAllByOrderId(orderId)).willReturn(Collections.singletonList(orderItemDto));
//
//        mockMvc.perform(get("/v1/order_items/" + orderId))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(orderItemDto))));
//    }
//
//    @Test
//    @DisplayName("GET /v1/order_items/{orderId}/{id} - Success")
//    void findById_ShouldReturnOrderItem() throws Exception {
//        given(orderItemService.findById(orderItemId)).willReturn(orderItemEntity);
//
//        mockMvc.perform(get("/v1/order_items/" + orderId + "/" + orderItemId))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(orderItemDto)));
//    }
//
//    @Test
//    @DisplayName("POST /v1/order_items/{orderId} - Success")
//    void create_ShouldReturnCreatedOrderItem() throws Exception {
//        given(orderItemService.create(any(OrderItemCreateDto.class), eq(orderId))).willReturn(orderItemEntity);
//
//        mockMvc.perform(post("/v1/order_items/" + orderId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andExpect(status().isCreated())
//                .andExpect(content().json(objectMapper.writeValueAsString(orderItemDto)));
//    }
//
//    @Test
//    @DisplayName("PUT /v1/order_items/{orderId} - Success")
//    void updateQuantity_ShouldReturnUpdatedOrderItem() throws Exception {
//        Integer newQuantity = 15;
//        orderItemDto.setQuantity(newQuantity);
//        given(orderItemService.updateQuantity(orderItemId, newQuantity)).willReturn(orderItemDto);
//
//        mockMvc.perform(put("/v1/order_items/" + orderId + "?id=" + orderItemId + "&quantity=" + newQuantity))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(orderItemDto)));
//    }
//
//    @Test
//    @DisplayName("DELETE /v1/order_items/{orderId} - Success")
//    void delete_ShouldReturnNoContent() throws Exception {
//        mockMvc.perform(delete("/v1/order_items/" + orderId + "?id=" + orderItemId))
//                .andExpect(status().isNoContent());
//    }
//}
