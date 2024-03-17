package org.shop.com.repository;
import org.shop.com.entity.ProductEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(long id);

    List<ProductEntity> findByCategory(String category, Sort optionsToSort);

    List<ProductEntity> findByPriceGreaterThanEqual(Double minPrice, Sort optionsToSort);

    List<ProductEntity> findByPriceLessThanEqual(Double maxPrice, Sort optionsToSort);

    @Query("SELECT p FROM ProductEntity p WHERE p.category = :category AND p.price BETWEEN :minPrice AND :maxPrice")
    List<ProductEntity> findByCategoryAndPriceBetween(@Param("category") String category,
                                                      @Param("minPrice") Double minPrice,
                                                      @Param("maxPrice") Double maxPrice, Sort sort);

    @Query("SELECT DISTINCT p.category FROM ProductEntity p")
    List<String> findAllCategories();
}