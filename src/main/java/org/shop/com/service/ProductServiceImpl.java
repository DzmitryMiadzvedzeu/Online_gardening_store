package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.entity.CategoryEntity;
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

    private final CategoryService categoryService;

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
    public ProductEntity findById(long id) {
        log.debug("Obtaining product with id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException("Can't find product with id " + id);
                });
    }

    @Override
    public ProductEntity create(ProductCreateDto productCreateDto) {
        log.debug("Creating product: {}", productCreateDto);
        CategoryEntity categoryById = categoryService.getById(productCreateDto.getCategoryId());
        ProductEntity createdProduct = ProductMapper.INSTANCE.createDtoToEntity(productCreateDto);
        createdProduct.setCategory(categoryById);
        return repository.save(createdProduct);
    }

    @Override
    public ProductEntity update(ProductEntity productEntity) {
        log.debug("Updating product: {}", productEntity);

        ProductEntity existingProduct = repository.findById(productEntity.getId())
                .orElseThrow(() -> new ProductNotFoundException("Can't find the product with id "
                        + productEntity.getId()));

        existingProduct.setName(productEntity.getName());
        existingProduct.setDescription(productEntity.getDescription());
        existingProduct.setPrice(productEntity.getPrice());
        existingProduct.setCategory(productEntity.getCategory());
        existingProduct.setImage(productEntity.getImage());
        return repository.save(existingProduct);
    }

    @Override
    public void delete(long id) {
        log.debug("Delete product with id {}", id);
        ProductEntity deletedProduct = findById(id);
        if (deletedProduct != null) {
            repository.delete(deletedProduct);
        } else {
            log.error("Product with id not found", id);
            throw new ProductNotFoundException("There is no product with ID " + id);
        }
    }
}