package org.shop.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private long id;

    private String deliveryAddress;

    private String deliveryMethod;

    private String contactPhone;

    private OrderStatus status;

    private Date createdAt;

    private Date updatedAt;
}