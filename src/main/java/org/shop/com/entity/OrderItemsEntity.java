package org.shop.com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderItems")
public class OrderItemsEntity {

    @Id
    long orderId;

    @Id
    long productId;
}
