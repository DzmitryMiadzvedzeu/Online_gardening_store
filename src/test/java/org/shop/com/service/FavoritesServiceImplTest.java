package org.shop.com.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.entity.FavoritesEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.FavoritesNotFoundException;
import org.shop.com.exceptions.ProductIllegalArgumentException;
import org.shop.com.repository.FavoritesJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritesServiceImplTest {

    @Mock
    private FavoritesJpaRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private FavoritesServiceImpl service;

    @Test
    void getUsersFavoritesByUserId_ShouldReturnFavoritesList() {
        Long userId = 1L;
        List<FavoritesEntity> expectedFavorites = new ArrayList<>();
        expectedFavorites.add(new FavoritesEntity());

        when(repository.findAllByUserEntityId(userId)).thenReturn(expectedFavorites);

        List<FavoritesEntity> result = service.getUsersFavoritesByUserId(userId);

        assertThat(result).isEqualTo(expectedFavorites);
        verify(repository, times(1)).findAllByUserEntityId(userId);
    }

    @Test
    void addIntoFavorites_ShouldAddProduct() {
        Long userId = 1L;
        Long productId = 1L;
        UserEntity user = new UserEntity();
        ProductEntity product = new ProductEntity();

        when(userService.findById(userId)).thenReturn(user);
        when(productService.findById(productId)).thenReturn(product);
        when(repository.findByUserEntityIdAndProductEntityId(userId, productId)).thenReturn(Optional.empty());

        service.addIntoFavorites(userId, productId);

        verify(repository, times(1)).save(any(FavoritesEntity.class));
    }

    @Test
    void addIntoFavorites_ShouldThrowException_IfProductAlreadyInFavorites() {
        Long userId = 1L;
        Long productId = 1L;
        when(repository.findByUserEntityIdAndProductEntityId(userId, productId)).thenReturn(Optional.of(new FavoritesEntity()));

        assertThatThrownBy(() -> service.addIntoFavorites(userId, productId))
                .isInstanceOf(ProductIllegalArgumentException.class)
                .hasMessageContaining("already in favorites");

        verify(repository, never()).save(any(FavoritesEntity.class));
    }

    @Test
    void removeProductFromFavorites_ShouldRemove_IfProductExists() {
        Long userId = 1L;
        Long productId = 1L;

        when(repository.existsByUserEntityIdAndProductEntityId(userId, productId)).thenReturn(true);

        service.removeProductFromFavorites(userId, productId);

        verify(repository, times(1)).deleteByUserEntityIdAndProductEntityId(userId, productId);
    }

    @Test
    void removeProductFromFavorites_ShouldThrowException_IfProductNotExists() {
        Long userId = 1L;
        Long productId = 1L;

        when(repository.existsByUserEntityIdAndProductEntityId(userId, productId)).thenReturn(false);

        assertThatThrownBy(() -> service.removeProductFromFavorites(userId, productId))
                .isInstanceOf(FavoritesNotFoundException.class)
                .hasMessageContaining("Favorites not found");

        verify(repository, never()).deleteByUserEntityIdAndProductEntityId(anyLong(), anyLong());
    }
}