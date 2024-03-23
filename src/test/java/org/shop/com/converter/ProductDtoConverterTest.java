package org.shop.com.converter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shop.com.dto.ProductCreateDto;
import org.shop.com.dto.ProductDto;
import org.shop.com.entity.CategoryEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.repository.CategoryJpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class ProductDtoConverterTest {

    private ProductDtoConverter converter;

    @Mock
    private CategoryJpaRepository categoryRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        converter = new ProductDtoConverter(categoryRepository);


        CategoryEntity category = new CategoryEntity(1L, "Category");
        when(categoryRepository.findByName("Category")).thenReturn(Optional.of(category));
    }

    @Test
    void testToDto() {
        CategoryEntity categoryEntity = new CategoryEntity(1L, "Category");

        ProductEntity entity = new ProductEntity();
        entity.setName("Name");
        entity.setDescription("Description");
        entity.setPrice(BigDecimal.valueOf(100));
        entity.setCategory(categoryEntity);
        entity.setImage("Image");

        ProductDto dto = converter.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(entity.getPrice(), dto.getPrice());
        assertEquals(categoryEntity.getName(), dto.getCategoryId());
        assertEquals(entity.getImage(), dto.getImage());
    }

    @Test
    void testCreateDtoToEntity() {
        ProductCreateDto createdDto = new ProductCreateDto("Name", "Description",
                                                        BigDecimal.valueOf(10.0), 2, "Image");
        ProductEntity entity = converter.createDtoToEntity(createdDto);

        assertNotNull(entity);
        assertEquals(createdDto.getName(), entity.getName());
        assertEquals(createdDto.getDescription(), entity.getDescription());
        assertEquals(createdDto.getPrice(), entity.getPrice());
        assertNotNull(entity.getCategory());
        assertEquals("Category", entity.getCategory().getName());
        assertEquals(createdDto.getImage(), entity.getImage());
    }

    @AfterEach
    public void destroyEach() {
        System.out.println("Call destroy each method");
    }
}