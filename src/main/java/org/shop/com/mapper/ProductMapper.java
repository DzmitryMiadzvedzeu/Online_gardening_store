package org.shop.com.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;

import java.beans.Transient;

@Mapper(componentModel = "spring")
public interface ProductMapper {

   ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);


   @Mapping(source = "name", target = "name")
   @Mapping(source = "description", target = "description")
   @Mapping(source = "price", target = "price")
   @Mapping(source = "image", target = "image")
   @Mapping(source = "category.categoryId", target = "categoryId")
   ProductDto toDto(ProductEntity productEntity);

   @Mapping(source = "name", target = "name")
   @Mapping(source = "description", target = "description")
   @Mapping(source = "price", target = "price")
   @Mapping(source = "image", target = "image")
//   @Mapping(source = "categoryId", target = "idCategory")
   ProductEntity createDtoToEntity(ProductCreateDto productCreateDto);

   @Mapping(source = "name", target = "name")
   @Mapping(source = "description", target = "description")
   @Mapping(source = "price", target = "price")
   @Mapping(source = "image", target = "image")
//   @Mapping(source = "categoryId", target = "category")
   ProductEntity toEntity(ProductDto dto);

}
