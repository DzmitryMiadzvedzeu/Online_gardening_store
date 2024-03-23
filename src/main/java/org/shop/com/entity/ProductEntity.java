package org.shop.com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal price;

    @NotBlank (message = "Image url can't be empty")
    private String image;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private BigDecimal discountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;


    public ProductEntity(long id, String name, String description, BigDecimal price, String image, CategoryEntity category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.discountPrice = calculateDiscountPrice(price);
        this.category = category;
    }

    private BigDecimal calculateDiscountPrice(BigDecimal price) {
        BigDecimal discount = new BigDecimal("0.10");
        BigDecimal startPrice = new BigDecimal("1000");
        if (price.compareTo(startPrice) > 0) {
            BigDecimal discountAmount = price.multiply(discount);
            return price.subtract(discountAmount);
        } else {
            return price;
        }
    }
}
