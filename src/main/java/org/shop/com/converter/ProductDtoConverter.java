package org.shop.com.converter;

import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoConverter implements ProductConverter<ProductEntity, ProductDto> {
    @Override
    public ProductDto toDto(ProductEntity entity) {
        return new ProductDto(entity.getId(), entity.getName(),
                entity.getDescription(), entity.getPrice(), entity.getCategory(),
                entity.getImage());
    }

    @Override
    public ProductEntity createDtoToEntity(ProductCreateDto productDto) {
        return new ProductEntity(productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getCategory(),
                productDto.getImage());
    }
}
