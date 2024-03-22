package org.shop.com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.shop.com.enums.OrderStatus;

import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "This field can't be empty")
    private String deliveryAddress;

    @NotBlank(message = "This field can't be empty")
    private String deliveryMethod;

    @NotBlank(message = "This field can't be empty")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$",
    message = "does not match phone number format")
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    public OrderEntity(String deliveryAddress, String deliveryMethod, String contactPhone) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
        this.contactPhone = contactPhone;
        this.status = OrderStatus.CREATED;
        this.createdAt = new Date();
        this.updatedAt = new Date();

    }
}
