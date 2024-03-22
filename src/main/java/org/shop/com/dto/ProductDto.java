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

}
