package org.shop.com.service;

import org.shop.com.entity.ProductEntity;

import java.util.List;

public interface ProductService {

    ProductEntity create(ProductEntity productEntity);

    List<ProductEntity> getAll(String category, Double minPrice, Double maxPrice, Boolean discount, String sort);

    ProductEntity delete(long id);

    ProductEntity findById(long id);
}
