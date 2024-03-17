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
        Sort optionsToSort = Sort.unsorted();
        if (sort!=null) {
            optionsToSort = Sort.by(sort);
        }

       List<ProductEntity> list = repository.findAll(optionsToSort);

        if (category != null) {
            list = repository.findByCategory(category, optionsToSort);
        }
        if (minPrice != null) {
            list = repository.findByPriceGreaterThanEqual(minPrice, optionsToSort);
        }
        if (maxPrice != null) {
            list = repository.findByPriceLessThanEqual(maxPrice, optionsToSort);
        }
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
