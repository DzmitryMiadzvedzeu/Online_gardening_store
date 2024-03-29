package org.shop.com.service;

import org.shop.com.entity.FavoritesEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.FavoritesNotFoundException;
import org.shop.com.exceptions.ProductIllegalArgumentException;
import org.shop.com.repository.FavoritesJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class FavoritesServiceImpl implements FavoritesService {


    private final FavoritesJpaRepository repository;
    private final UserService userService;
    private final ProductService productService;

    public FavoritesServiceImpl(FavoritesJpaRepository repository, UserService userService, ProductService productService) {
        this.repository = repository;
        this.userService = userService;
        this.productService = productService;
    }


    @Override
    public List<FavoritesEntity> getUsersFavoritesByUserId(Long userId) {
        return repository.findAllByUserEntityId(userId);
    }

    @Override
    public FavoritesEntity addIntoFavorites(Long userId, Long productId) {
        UserEntity user = userService.findById(userId);
        ProductEntity product = productService.findById(productId);
        repository.findByUserEntityIdAndProductEntityId(userId, productId).ifPresent(f -> {
            throw new ProductIllegalArgumentException("This product is already in favorites");
        });
        FavoritesEntity favorite = new FavoritesEntity();
        favorite.setUserEntity(user);
        favorite.setProductEntity(product);
        return repository.save(favorite);
    }


    @Override
    @Transactional
    public void removeProductFromFavorites(Long userId, Long productId) {
        boolean exists = repository.existsByUserEntityIdAndProductEntityId(userId, productId);
        if (!exists){
            throw new FavoritesNotFoundException("Favorites not found");
        }
        repository.deleteByUserEntityIdAndProductEntityId(userId, productId);
    }
}
