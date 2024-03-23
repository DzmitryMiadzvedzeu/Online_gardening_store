package org.shop.com.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.entity.ProductEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.mapper.ProductMapper;
import org.shop.com.repository.ProductJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository repository;

    @Override
    public ProductEntity create(ProductCreateDto productCreateDto) {
        log.debug("Creating product: {}", productCreateDto);
        ProductEntity createdProduct = ProductMapper.INSTANCE.createDtoToEntity(productCreateDto);
        ProductEntity savedProduct = repository.save(createdProduct);
        log.debug("Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    @Override
    public List<ProductEntity> getAll(String category, BigDecimal minPrice, BigDecimal maxPrice,
                                      BigDecimal discountPrice, String sort) {
        log.debug("Obtaining all products");
        Sort optionsToSort = Sort.unsorted();
        if (sort != null) {
            optionsToSort = Sort.by(sort);
        }
        List<ProductEntity> productList = repository.findAll(optionsToSort);

        if (category != null) {
            log.debug("Filtering products by category: {}", category);
            productList = repository.findByCategory(category, optionsToSort);
        }
        if (minPrice != null) {
            log.debug("Filtering products by min price: {}", minPrice);
            productList = repository.findByPriceGreaterThanEqual(minPrice, optionsToSort);
        }
        if (maxPrice != null) {
            log.debug("Filtering products by max price: {}", maxPrice);
            productList = repository.findByPriceLessThanEqual(maxPrice, optionsToSort);
        }
        if (discountPrice != null) {
            log.debug("Filtering products by discount price: {}", discountPrice);
            productList = repository.findByDiscountPrice(discountPrice, optionsToSort);
        }
        log.debug("Returning {} products", productList.size());
        return productList;
    }

    @Override
    public ProductEntity delete(long id) {
        log.debug("Delete product with id {}", id);
        ProductEntity deletedProduct = findById(id);
        if (deletedProduct != null) {
            repository.delete(deletedProduct);
            log.debug("Product with id  {} deleted successfully", id);
        } else {
            log.error("Product with id  not found", id);
            throw new ProductNotFoundException("There is no product with ID " + id);
        }
        return deletedProduct;
    }

    @Override
    public ProductEntity findById(long id) {
        log.debug("Obtaining product with id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException("Can't find product with id " + id);
                });
    }

    @Override
    public ProductEntity update(ProductEntity productEntity) {
        log.debug("Updating product: {}", productEntity);
        if (findById(productEntity.getId()) == null) {
            log.error("Product with id {} not found", productEntity.getId());
            throw new ProductNotFoundException("Can't find the product");
        }
        ProductEntity updatedProduct = repository.save(productEntity);
        log.debug("Product updated successfully: {}", updatedProduct);
        return updatedProduct;
    }
}