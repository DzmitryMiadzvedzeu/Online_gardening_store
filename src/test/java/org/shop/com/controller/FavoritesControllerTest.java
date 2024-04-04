package org.shop.com.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.dto.FavoritesCreateDto;
import org.shop.com.dto.FavoritesDto;
import org.shop.com.entity.FavoritesEntity;
import org.shop.com.mapper.FavoritesMapper;
import org.shop.com.service.FavoritesService;
import org.shop.com.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoritesControllerTest {

    @Mock
    private FavoritesService favoritesService;

    @Mock
    private FavoritesMapper favoritesMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoritesController favoritesController;

    private final Long userId = 1L;

    private FavoritesEntity favoritesEntity;

    private FavoritesDto favoritesDto;

    @BeforeEach
    void setUp() {
        favoritesEntity = new FavoritesEntity();
        favoritesDto = new FavoritesDto();
        favoritesDto.setUserId(userId);
        favoritesDto.setProductId(1L);

        when(userService.getCurrentUserId()).thenReturn(userId);
    }

    @Test
    void addProductToFavorites_ShouldReturnCreated() {
        Long productId = 1L;
        FavoritesCreateDto favoritesCreateDto = new FavoritesCreateDto();
        favoritesCreateDto.setProductId(productId);

        when(favoritesService.addIntoFavorites(userId, productId)).thenReturn(favoritesEntity);
        when(favoritesMapper.toDto(favoritesEntity)).thenReturn(favoritesDto);

        ResponseEntity<FavoritesDto> response = favoritesController.addProductToFavorites(favoritesCreateDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(favoritesDto);
    }

    @Test
    void getAllFavorites_ShouldReturnFavoritesList() {
        when(favoritesService.getUsersFavoritesByUserId(userId)).thenReturn(Arrays.asList(favoritesEntity));
        when(favoritesMapper.toDto(favoritesEntity)).thenReturn(favoritesDto);

        ResponseEntity<List<FavoritesDto>> response = favoritesController.getAllFavorites();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0)).isEqualTo(favoritesDto);
    }

    @Test
    void removeProductFromFavorites_ShouldReturnNoContent() {
        ResponseEntity<Void> response = favoritesController.removeProductFromFavorites(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
