package org.shop.com.service;

import org.shop.com.entity.FavoritesEntity;

import java.util.List;

public interface FavoritesService {

    List<FavoritesEntity> getUsersFavoritesByUserId (Long userId);

    FavoritesEntity addIntoFavorites(Long userId, Long productId);

    void removeProductFromFavorites(Long userId, Long productId);



}
