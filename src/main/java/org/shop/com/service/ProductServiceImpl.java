package org.shop.com.service;
import lombok.RequiredArgsConstructor;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.repository.ProductJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductJpaRepository repository;

    @Override
    public ProductEntity create(ProductEntity productEntity) {
        return repository.save(productEntity);
    }

    @Override
    public List<ProductEntity> getAll(String category, Double minPrice, Double maxPrice, String sort) {
        // Применяем все фильтры одновременно в одном запросе к базе данных
        List<ProductEntity> list = repository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, Sort.unsorted());
        return list;
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

    @Override
    public ProductEntity update(ProductEntity productEntity) {
        if (findById(productEntity.getId()) == null) {
            throw new ProductNotFoundException("Can't find the product");
        }
        return repository.save(productEntity);
    }

    @Override
    public List<String> listAllCategories() {
        return repository.findAllCategories();
    }
}
