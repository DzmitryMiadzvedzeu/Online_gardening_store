package org.shop.com.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

   ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

   ProductDto toDto(ProductEntity productEntity);

   ProductEntity createDtoToEntity(ProductCreateDto productCreateDto);

   ProductEntity toEntity(ProductDto dto);

}
