//package org.shop.com.converter;
//import org.shop.com.dto.ProductCreateDto;
//import org.shop.com.dto.ProductDto;
//import org.shop.com.entity.CategoryEntity;
//import org.shop.com.entity.ProductEntity;
//import org.shop.com.exceptions.CategoryNotFoundException;
//import org.shop.com.repository.CategoryJpaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import java.time.LocalDateTime;
//
//@Component
//public class ProductDtoConverter implements ProductConverter<ProductEntity, ProductDto> {
//
//    private final CategoryJpaRepository categoryRepository;
//
//    @Autowired
//    public ProductDtoConverter(CategoryJpaRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    @Override
//    public ProductDto toDto(ProductEntity entity) {
//        ProductDto productDto = new ProductDto();
//        productDto.setId(entity.getId());
//        productDto.setName(entity.getName());
//        productDto.setDescription(entity.getDescription());
//        productDto.setPrice(entity.getPrice());
//        productDto.setImage(entity.getImage());
//        productDto.setCreatedAt(entity.getCreatedAt());
//        productDto.setUpdatedAt(entity.getUpdatedAt());
//        productDto.setDiscountPrice(entity.getDiscountPrice());
//        productDto.setCategoryId(entity.getCategory() != null ? entity.getCategory().getCategoryId() : 0);
//        return productDto;
//    }
//
//    @Override
//    public ProductEntity createDtoToEntity(ProductCreateDto productDto) {
//        ProductEntity productEntity = new ProductEntity();
//        productEntity.setName(productDto.getName());
//        productEntity.setDescription(productDto.getDescription());
//        productEntity.setPrice(productDto.getPrice());
//        productEntity.setImage(productDto.getImage());
//        CategoryEntity category = categoryRepository.findById(productDto.getCategoryId())
//                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
//        productEntity.setCategory(category);
//        return productEntity;
//    }
//
//    @Override
//    public ProductEntity toEntity(ProductDto dto) {
//        ProductEntity productEntity = new ProductEntity();
//        productEntity.setId(dto.getId());
//        productEntity.setName(dto.getName());
//        productEntity.setDescription(dto.getDescription());
//        productEntity.setPrice(dto.getPrice());
//        productEntity.setImage(dto.getImage());
//        productEntity.setCreatedAt(LocalDateTime.now());
//        productEntity.setUpdatedAt(LocalDateTime.now());
//        productEntity.setDiscountPrice(dto.getDiscountPrice());
//        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
//        productEntity.setCategory(category);
//        return productEntity;
//    }
//
//    private CategoryEntity findCategoryByName(String categoryName) {
//        if (categoryName != null && !categoryName.isEmpty()) {
//            return categoryRepository.findByName(categoryName)
//                    .orElseThrow(() -> new RuntimeException("Category not found"));
//        }
//        return null;
//    }
//}