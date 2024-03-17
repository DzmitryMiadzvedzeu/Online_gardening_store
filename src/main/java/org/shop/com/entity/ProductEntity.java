package org.shop.com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Product name can't be empty")
    private String name;

    @NotBlank(message = "Product description can't be empty")
    private String description;

    @Max(value = 100000, message = "Incorrect max value")
    @Min(value = 1, message = "Incorrect min value")
    private double price;

   // @NotBlank (message = "Category can't be empty")
    //I think there must be Category, not String
   // private String category;

    @NotBlank (message = "Image url can't be empty")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;


}
