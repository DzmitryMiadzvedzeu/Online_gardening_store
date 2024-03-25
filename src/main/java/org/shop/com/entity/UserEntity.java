package org.shop.com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.shop.com.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "This field can't be empty")
    @Length(max = 255, min = 2, message = "Name cant be longer than 255 characters and less than 2 characters")
    private String name;

    @Email
    @Column(unique = true)
    @NotBlank(message = "This field can't be empty")
    @Length(max = 255, message = "e-mail cant be longer than 255 characters")
    private String email;

    @NotBlank(message = "This field can't be empty")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$",
            message = "irregular format of this field")
    private String phoneNumber;

    @NotBlank(message = "This field can't be empty")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CartEntity cart;

    public UserEntity(String name, String email, String phoneNumber, String passwordHash) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
