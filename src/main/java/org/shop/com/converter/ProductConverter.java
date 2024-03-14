package org.shop.com.converter;
import org.shop.com.dto.ProductCreateDto;

public interface ProductConverter <Entity, Dto>{
    Dto toDto (Entity entity);

    Entity createDtoToEntity(ProductCreateDto productCreateDto);
}
