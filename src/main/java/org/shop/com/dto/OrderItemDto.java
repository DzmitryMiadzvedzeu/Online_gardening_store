package org.shop.com.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long id;

    private Long productId;

    private Integer quantity;

    private BigDecimal priceAtPurchase;

}
