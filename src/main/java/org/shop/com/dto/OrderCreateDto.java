package org.shop.com.dto;

/*
{
  "items": [ на первом спринте это не реализуется
    {
      "productId": "string",
      "quantity": "number"
    }
  ],на первом спринте это не реализуется
  "deliveryAddress": "string",
  "deliveryMethod": "string"
}
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shop.com.enums.OrderStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {

    private String deliveryAddress;

    private String deliveryMethod;

    private String contactPhone;

    private OrderStatus status;

    private Date createdAt;

    private Date updatedAt;

}
