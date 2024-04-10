package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.entity.FavoritesEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.FavoritesNotFoundException;
import org.shop.com.exceptions.ProductIllegalArgumentException;
import org.shop.com.repository.FavoritesJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesJpaRepository repository;

    private final UserService userService;

    private final ProductService productService;

    @Override
    public List<FavoritesEntity> getUsersFavoritesByUserId(Long userId) {
        log.debug("Retrieving favorites for user with ID: {}", userId);
        return repository.findAllByUserEntityId(userId);
    }

    @Override
    public FavoritesEntity addIntoFavorites(Long userId, Long productId) {
        log.debug("Attempting to add product with ID: {} to favorites for user with ID: {}", productId, userId);
        UserEntity user = userService.findById(userId);
        ProductEntity product = productService.findById(productId);
        repository.findByUserEntityIdAndProductEntityId(userId, productId).ifPresent(f -> {
            throw new ProductIllegalArgumentException("This product is already in favorites");
        });
        FavoritesEntity favorite = new FavoritesEntity();
        favorite.setUserEntity(user);
        favorite.setProductEntity(product);
        FavoritesEntity savedFavorite = repository.save(favorite);
        return savedFavorite;
    }

    @Override
    @Transactional
    public void removeProductFromFavorites(Long userId, Long productId) {
        log.debug("Attempting to remove product with ID: {} from favorites for user with ID: {}", productId, userId);
        boolean exists = repository.existsByUserEntityIdAndProductEntityId(userId, productId);
        if (!exists) {
            throw new FavoritesNotFoundException("Favorites not found");
        }
        repository.deleteByUserEntityIdAndProductEntityId(userId, productId);
    }
}