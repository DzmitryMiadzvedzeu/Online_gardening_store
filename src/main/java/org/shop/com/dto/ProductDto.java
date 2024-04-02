package org.shop.com.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    private long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BigDecimal discountPrice;

    private long categoryId;

    public ProductDto(long id, String name, String description, BigDecimal price, String image, BigDecimal discountPrice, long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.discountPrice = discountPrice;
        this.categoryId = categoryId;
    }

    public ProductDto( String name, String description, BigDecimal price, String image,  BigDecimal discountPrice) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.discountPrice = discountPrice;
    }
}

