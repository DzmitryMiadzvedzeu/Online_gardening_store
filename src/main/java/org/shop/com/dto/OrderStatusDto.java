package org.shop.com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shop.com.enums.OrderStatus;

@Setter
@Getter
@AllArgsConstructor
public class OrderStatusDto {

    private long id;

    private OrderStatus status;
}