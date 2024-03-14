package org.shop.com.service;
import lombok.RequiredArgsConstructor;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.repository.ProductJpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductJpaRepository repository;

    @Override
    public ProductEntity create(ProductEntity productEntity) {
        return repository.save(productEntity);
    }

    @Override
    public List<ProductEntity> getAll(String category, Double minPrice, Double maxPrice, Boolean discount, String sort) {
        // Дополнительная логика для фильтрации, сортировки и т.д.
        return repository.findAll();
    }

    @Override
    public ProductEntity delete(long id) {
        ProductEntity deletedProduct = findById(id);
        if(deletedProduct != null) {
            repository.delete(deletedProduct);
        } else {
            new ProductNotFoundException("There is no product with id " + id);
        }
        return deletedProduct;
    }

    @Override
    public ProductEntity findById(long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Can't find product with id " + id));
    }
}
