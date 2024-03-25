package org.shop.com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "favorites")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoritesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    // перенести это поле в UserEntity
//    @OneToMany(mappedBy = "Users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<FavoritesEntity> favoritesEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;


// перенести это поле в сущность ProductEntity
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<FavoritesEntity> favorites = new ArrayList<>();


}
