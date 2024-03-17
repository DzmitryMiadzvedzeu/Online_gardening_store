package org.shop.com.repository;
import org.shop.com.entity.ProductEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(long id);

    List<ProductEntity> findByCategory(String category, Sort optionsToSort);

    List<ProductEntity> findByPriceGreaterThanEqual (Double minPrice, Sort optionsToSort);

    List<ProductEntity> findByPriceLessThanEqual (Double maxPrice, Sort optionsToSort);

    @Query("SELECT DISTINCT product.category FROM ProductEntity product")
    List<String> findAllCategories();

 //   List<ProductEntity> findByDiscount (Boolean discount, Sort optionsToSort);
}
