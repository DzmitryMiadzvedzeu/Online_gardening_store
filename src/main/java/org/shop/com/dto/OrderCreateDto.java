package org.shop.com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {

    private String deliveryAddress;

    private String deliveryMethod;

    private String contactPhone;

    private List<OrderItemCreateDto> items;
}