package org.shop.com.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

   ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);


   @Mapping(source = "name", target = "name")
   @Mapping(source = "description", target = "description")
   @Mapping(source = "price", target = "price")
   @Mapping(source = "image", target = "image")
   @Mapping(source = "category.id", target = "categoryId")
//   @Mapping(target = "createdAt", ignore = true)
   ProductDto toDto(ProductEntity productEntity);

   @Mapping(source = "name", target = "name")
   @Mapping(source = "description", target = "description")
   @Mapping(source = "price", target = "price")
   @Mapping(source = "image", target = "image")
   @Mapping(source = "categoryId", target = "category.id")
   ProductEntity createDtoToEntity(ProductCreateDto productCreateDto);

   @Mapping(source = "name", target = "name")
   @Mapping(source = "description", target = "description")
   @Mapping(source = "price", target = "price")
   @Mapping(source = "image", target = "image")
   @Mapping(source = "categoryId", target = "category.id")
//   @Mapping(target = "createdAt", ignore = true)
   ProductEntity toEntity(ProductDto dto);

   @Mapping(target = "id", ignore = true)
   @Mapping(target = "createdAt", ignore = true) // Игнорируем, чтобы не перезаписывать
   @Mapping(target = "updatedAt", ignore = true) // Игнорируем, чтобы Hibernate сам обновил
   void updateEntityFromDto(ProductDto dto, @MappingTarget ProductEntity entity);

}
