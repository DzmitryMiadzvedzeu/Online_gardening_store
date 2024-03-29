package org.shop.com.controller;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.FavoritesCreateDto;
import org.shop.com.dto.FavoritesDto;
import org.shop.com.entity.FavoritesEntity;
import org.shop.com.service.FavoritesService;
import org.shop.com.service.UserService;
import org.shop.com.mapper.FavoritesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
// http://localhost:8080/v1/favorites
@Slf4j
@RestController
@RequestMapping("/v1/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final FavoritesMapper favoritesMapper;
    private final UserService userService;

    @Autowired
    public FavoritesController(FavoritesService favoritesService, FavoritesMapper favoritesMapper, UserService userService) {
        this.favoritesService = favoritesService;
        this.favoritesMapper = favoritesMapper;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<FavoritesDto> addProductToFavorites(@RequestBody FavoritesCreateDto favoritesCreateDto) {
        log.debug("Adding product {} to favorites for current user", favoritesCreateDto.getProductId());
        Long userId = userService.getCurrentUserId(); // Получаем ID текущего пользователя
        FavoritesEntity savedFavorite = favoritesService.addIntoFavorites(userId, favoritesCreateDto.getProductId());
        FavoritesDto responseDto = favoritesMapper.toDto(savedFavorite);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FavoritesDto>> getAllFavorites() {
        log.debug("Getting all favorites for current user");
        Long userId = userService.getCurrentUserId(); // Получаем ID текущего пользователя(,,)
        List<FavoritesDto> favoritesDtos = favoritesService.getUsersFavoritesByUserId(userId).stream()
                .map(favoritesMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(favoritesDtos);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProductFromFavorites(@PathVariable Long productId) {
        log.debug("Removing product {} from favorites for current user", productId);
        Long userId = userService.getCurrentUserId(); // Получаем ID текущего пользователя(,,)
        favoritesService.removeProductFromFavorites(userId, productId);
        return ResponseEntity.noContent().build();
    }
}