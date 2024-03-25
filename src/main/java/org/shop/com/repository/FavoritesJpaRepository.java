package org.shop.com.repository;

import org.shop.com.entity.FavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesJpaRepository extends JpaRepository<FavoritesEntity, Long> {

    List<FavoritesEntity> findAllByUserId (Long userId);
    Optional<FavoritesEntity> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserIdAndProductId(Long userId, Long productId);

}
