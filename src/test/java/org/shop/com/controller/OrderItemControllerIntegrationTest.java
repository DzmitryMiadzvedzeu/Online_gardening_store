//package org.shop.com.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.shop.com.ShopApp;
//import org.shop.com.dto.OrderItemCreateDto;
//import org.shop.com.entity.*;
//import org.shop.com.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = ShopApp.class)
//@AutoConfigureMockMvc
//public class OrderItemControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private OrderJpaRepository orderRepository;
//
//    @Autowired
//    private ProductJpaRepository productRepository;
//
//    @Autowired
//    private OrderItemJpaRepository orderItemRepository;
//
//    @Autowired
//    private CategoryJpaRepository categoryRepository;
//
//    @Autowired
//    private UserJpaRepository userJpaRepository;
//
//    private CategoryEntity category = new CategoryEntity();
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    private ProductEntity product = new ProductEntity();
//
//    private OrderEntity order = new OrderEntity();
//
//    private  UserEntity userEntity = new UserEntity();
//
//    @BeforeEach
//    void setUp() throws Exception {
//        orderItemRepository.deleteAllInBatch();
//        productRepository.deleteAllInBatch();
//        orderRepository.deleteAllInBatch();
//        categoryRepository.deleteAllInBatch();
//
//        category.setName("Electronics");
//        categoryRepository.save(category);
//        product.setName("Laptop");
//        product.setPrice(new BigDecimal("1200.00"));
//        product.setCategory(category);
//        product.setDescription("Description");
//        product.setImage("Image");
//        product.setDiscountPrice(new BigDecimal("20"));
//        productRepository.save(product);
//
//        userEntity.setName("Name");
//        userEntity.setEmail("andriyets2004@gmail.com");
//        userEntity.setPhoneNumber("+86456765468");
//        userEntity.setPasswordHash("password");
//        userJpaRepository.save(userEntity);
//
//        order.setDeliveryAddress("123 Main St");
//        order.setDeliveryMethod("UPS");
//        order.setContactPhone("1234567890");
//        order.setUserEntity(userEntity);
//        orderRepository.save(order);
//    }
//
//    @Test
//    void getAllOrderItems_ShouldReturnOrderItems() throws Exception {
//        List<OrderItemEntity> orderItems = createTestOrderItems();
//
//        mockMvc.perform(get("/v1/order_items/" + orderItems.get(0).getOrder().getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].productId", is(orderItems.get(0).getProduct().getId().intValue())))
//                .andExpect(jsonPath("$[0].quantity", is(orderItems.get(0).getQuantity())));
//    }
//
//    private List<OrderItemEntity> createTestOrderItems() {
//        OrderEntity order = orderRepository.findAll().get(0);
//        ProductEntity product = productRepository.findAll().get(0);
//
//        OrderItemEntity orderItem = new OrderItemEntity();
//        orderItem.setOrder(order);
//        orderItem.setProduct(product);
//        orderItem.setQuantity(5);
//        orderItem.setPriceAtPurchase(new BigDecimal("1000.00"));
//        orderItemRepository.save(orderItem);
//
//        return List.of(orderItem);
//    }
//
//    @Test
//    void getOrderItemById_ShouldReturnOrderItem() throws Exception {
//        OrderItemEntity orderItem = createTestOrderItems().get(0);
//
//        mockMvc.perform(get("/v1/order_items/" + orderItem.getOrder().getId() + "/" + orderItem.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.productId", is(orderItem.getProduct().getId().intValue())))
//                .andExpect(jsonPath("$.quantity", is(orderItem.getQuantity())))
//                .andExpect(jsonPath("$.priceAtPurchase", is(1000)));
//    }
//
//    @Test
//    void createOrderItem_ShouldReturnCreatedOrderItem() throws Exception {
//        OrderItemCreateDto createDto = new OrderItemCreateDto();
//        createDto.setProductId(product.getId());
//        createDto.setQuantity(2);
//        createDto.setPriceAtPurchase(new BigDecimal("900.00"));
//
//        mockMvc.perform(post("/v1/order_items/" + order.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.productId", is(createDto.getProductId().intValue())))
//                .andExpect(jsonPath("$.quantity", is(createDto.getQuantity())))
//                .andExpect(jsonPath("$.priceAtPurchase", is(900.00)));
//    }
//
//    @Test
//    void updateOrderItemQuantity_ShouldUpdateQuantity() throws Exception {
//        OrderItemEntity orderItem = createTestOrderItems().get(0);
//
//        mockMvc.perform(put("/v1/order_items/" + orderItem.getOrder().getId() + "?id=" + orderItem.getId() + "&quantity=20"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.quantity", is(20)));
//    }
//
//    @Test
//    void deleteOrderItem_ShouldDeleteOrderItem() throws Exception {
//        OrderItemEntity orderItem = createTestOrderItems().get(0);
//
//        mockMvc.perform(delete("/v1/order_items/" + orderItem.getOrder().getId() + "?id=" + orderItem.getId()))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(get("/v1/order_items/" + orderItem.getOrder().getId() + "/" + orderItem.getId()))
//                .andExpect(status().isNotFound());
//    }
//}

