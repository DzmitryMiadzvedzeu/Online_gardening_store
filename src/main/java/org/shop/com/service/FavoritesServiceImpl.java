package org.shop.com.service;

import org.shop.com.entity.FavoritesEntity;
import org.shop.com.entity.ProductEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.ProductNotFoundException;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.repository.FavoritesJpaRepository;
import org.shop.com.repository.ProductJpaRepository;
import org.shop.com.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FavoritesServiceImpl implements FavoritesService {


    private final FavoritesJpaRepository repository;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    public FavoritesServiceImpl(FavoritesJpaRepository repository, UserJpaRepository userJpaRepository, ProductJpaRepository productJpaRepository) {
        this.repository = repository;
        this.userJpaRepository = userJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }


    @Override
    public List<FavoritesEntity> getUsersFavoritesByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public FavoritesEntity addIntoFavorites(Long userId, Long productId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        ProductEntity product = productJpaRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        FavoritesEntity favorite = new FavoritesEntity();
        favorite.setUserEntity(user);
        favorite.setProductEntity(product);
        return repository.save(favorite);
    }

    @Override
    public void removeProductFromFavorites(Long userId, Long productId) {
        repository.deleteByUserIdAndProductId(userId, productId);
    }
}
