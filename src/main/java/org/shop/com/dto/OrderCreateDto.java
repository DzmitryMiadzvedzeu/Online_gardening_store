package org.shop.com.dto;

/*
{
  "items": [ на первом спринте это не реализуется
    {
      "productId": "string",
      "quantity": "number"
    }
  ],
  "deliveryAddress": "string",
  "deliveryMethod": "string"
}
 */

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateDto {

    private String deliveryAddress;
    private String deliveryMethod;

}
