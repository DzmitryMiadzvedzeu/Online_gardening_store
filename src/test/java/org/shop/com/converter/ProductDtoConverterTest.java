package org.shop.com.converter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.ProductEntity;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ProductDtoConverterTest {

    private ProductDtoConverter converter;

    @BeforeEach
    public void init() {
       converter = new ProductDtoConverter();
    }
    @Test
    void testToDto() {
        ProductEntity entity = new ProductEntity( "Name", "Description", 10.0, "Category", "Image");
        ProductDto dto = converter.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(entity.getPrice(), dto.getPrice());
        assertEquals(entity.getCategory(), dto.getCategory());
        assertEquals(entity.getImage(), dto.getImage());
    }

    @Test
    void testCreateDtoToEntity() {
        ProductCreateDto createdDto = new ProductCreateDto("Name", "Description", 10.0, "Category", "Image");
        ProductEntity entity = converter.createDtoToEntity(createdDto);

        assertNotNull(entity);
        assertEquals(createdDto.getName(), entity.getName());
        assertEquals(createdDto.getDescription(), entity.getDescription());
        assertEquals(createdDto.getPrice(), entity.getPrice());
        assertEquals(createdDto.getCategory(), entity.getCategory());
        assertEquals(createdDto.getImage(), entity.getImage());
    }

    @AfterEach
    public void destroyEach() {
        System.out.println("Call destroy each method ");
    }
}

