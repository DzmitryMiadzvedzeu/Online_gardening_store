package org.shop.com.converter;

import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.repository.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class ProductDtoConverter implements ProductConverter<ProductEntity, ProductDto> {

    private final CategoryJpaRepository categoryRepository;

    @Autowired
    public ProductDtoConverter(CategoryJpaRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDto toDto(ProductEntity entity) {
        String categoryName = entity.getCategory() != null ? entity.getCategory().getName() : null;
        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                categoryName,
                entity.getImage()
        );
    }

    @Override
    public ProductEntity createDtoToEntity(ProductCreateDto productDto) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setCategory(findCategoryByName(productDto.getCategory()));
        productEntity.setImage(productDto.getImage());
        return productEntity;
    }

    @Override
    public ProductEntity toEntity(ProductDto dto) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(dto.getId());
        productEntity.setName(dto.getName());
        productEntity.setDescription(dto.getDescription());
        productEntity.setPrice(dto.getPrice());
        productEntity.setCategory(findCategoryByName(dto.getCategory()));
        productEntity.setImage(dto.getImage());
        return productEntity;
    }

    private CategoryEntity findCategoryByName(String categoryName) {
        if (categoryName != null && !categoryName.isEmpty()) {
            return categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }
        return null;
    }
}