package org.shop.com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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
    @NotNull
    private String deliveryAddress;

    @NotBlank(message = "This field can't be empty")
    @NotNull
    private String deliveryMethod;

    @NotBlank(message = "This field can't be empty")
    @NotNull
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$",
    message = "does not match phone number format")
    private String contactPhone;

    @NotEmpty(message = "This field can't be empty")
    private Enum status;

    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent
//    @PrePersist
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent
//    @PreUpdate
    private Date updatedAt;

}
