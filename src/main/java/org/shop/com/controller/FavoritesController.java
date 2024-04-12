package org.shop.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.FavoritesCreateDto;
import org.shop.com.dto.FavoritesDto;
import org.shop.com.entity.FavoritesEntity;
import org.shop.com.service.FavoritesService;
import org.shop.com.service.UserService;
import org.shop.com.mapper.FavoritesMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// http://localhost:8080/v1/favorites
@Tag(name = "Favorites Controller", description = "Controller for favorites operations")
@Slf4j
@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;

    private final FavoritesMapper favoritesMapper;

    private final UserService userService;

    @Operation(summary = "Add a product to favorites", description = "Adds a specified product to the current user's favorites.")
    @ApiResponse(responseCode = "201", description = "Product added to favorites",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FavoritesDto.class)))
    @PostMapping
    public ResponseEntity<FavoritesDto> addProductToFavorites
            (@RequestBody FavoritesCreateDto favoritesCreateDto) {
        log.debug("Adding product {} to favorites for current user",
                favoritesCreateDto.getProductId());
        Long userId = userService.getCurrentUserId(); // Получаем ID текущего пользователя
        FavoritesEntity savedFavorite = favoritesService.addIntoFavorites(userId,
                favoritesCreateDto.getProductId());
        FavoritesDto responseDto = favoritesMapper.toDto(savedFavorite);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all favorites", description = "Retrieves all favorites for the current user.")
    @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FavoritesDto[].class)))
    @GetMapping
    public ResponseEntity<List<FavoritesDto>> getAll() {
        log.debug("Getting all favorites for current user");
        Long userId = userService.getCurrentUserId(); // Получаем ID текущего пользователя(,,)
        List<FavoritesDto> favoritesDtos = favoritesService.getUsersFavoritesByUserId(userId)
                .stream()
                .map(favoritesMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(favoritesDtos);
    }

    @Operation(summary = "Remove a product from favorites", description = "Removes a specified product from the current user's favorites.")
    @ApiResponse(responseCode = "204", description = "Product removed from favorites")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProductFromFavorites(@PathVariable Long productId) {
        log.debug("Removing product {} from favorites for current user", productId);
        Long userId = userService.getCurrentUserId(); // Получаем ID текущего пользователя(,,)
        favoritesService.removeProductFromFavorites(userId, productId);
        return ResponseEntity.noContent().build();
    }
}