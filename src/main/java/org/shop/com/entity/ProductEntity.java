package org.shop.com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private BigDecimal discountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems;

    public ProductEntity(String name, String description, BigDecimal price, String image, CategoryEntity category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
    }
}
