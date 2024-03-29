package org.shop.com.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.shop.com.dto.FavoritesCreateDto;
import org.shop.com.dto.FavoritesDto;
import org.shop.com.entity.FavoritesEntity;

@Mapper(componentModel = "spring")
public interface FavoritesMapper {

    FavoritesMapper INSTANCE = Mappers.getMapper(FavoritesMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "userEntity.id")
    @Mapping(source = "productId", target = "productEntity.id")
    FavoritesEntity toEntity(FavoritesCreateDto favoritesCreateDto);

    @Mapping(source = "userEntity.id", target = "userId")
    @Mapping(source = "productEntity.id", target = "productId")
    FavoritesDto toDto(FavoritesEntity favoritesEntity);

    @Mapping(source = "userEntity.id", target = "userId")
    @Mapping(source = "productEntity.id", target = "productId")
    FavoritesDto toDtoWithoutId(FavoritesEntity favoritesEntity);
}