package org.shop.com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  ProductCreateDto {

    private String name;

    private String description;

    private double price;

    private String category;

    private String image;

}
