package org.shop.com.repository;

import org.shop.com.entity.ProductEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategory(String category, Sort optionsToSort);

    List<ProductEntity> findByPriceGreaterThanEqual(BigDecimal minPrice, Sort optionsToSort);

    List<ProductEntity> findByPriceLessThanEqual(BigDecimal maxPrice, Sort optionsToSort);

    List<ProductEntity> findByDiscountPrice(BigDecimal discountPrice, Sort optionsToSort);
}