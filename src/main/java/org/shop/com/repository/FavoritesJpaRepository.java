package org.shop.com.repository;

import org.shop.com.entity.FavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesJpaRepository extends JpaRepository<FavoritesEntity, Long> {

    List<FavoritesEntity> findAllByUserEntityId(Long userId);

    Optional<FavoritesEntity> findByUserEntityIdAndProductEntityId(Long userId, Long productId);

    void deleteByUserEntityIdAndProductEntityId(Long userId, Long productId);

    boolean existsByUserEntityIdAndProductEntityId(Long userId, Long productId);
}