package org.shop.com.repository;

import org.shop.com.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserId(Long userId);
}