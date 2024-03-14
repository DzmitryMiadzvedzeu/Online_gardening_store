package org.shop.com.repository;
import org.shop.com.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findById(long id);

}
