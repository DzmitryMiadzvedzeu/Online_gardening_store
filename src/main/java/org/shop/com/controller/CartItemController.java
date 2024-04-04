package org.shop.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.exceptions.CartItemsInvalidArgumentException;
import org.shop.com.exceptions.CartItemsNotFoundException;
import org.shop.com.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/cartItems")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @Operation(summary = "Add cart item to a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartItemDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body or parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CartItemDto> add(@RequestBody CartItemCreateDto cartItemCreateDto,
                                           @RequestParam Long cartId) {
        log.info("Adding cart item with productId: {} and quantity: {} to cartId: {}",
                cartItemCreateDto.getProductId(), cartItemCreateDto.getQuantity(), cartId);
        CartItemDto cartItemDto = cartItemService.add(cartItemCreateDto, cartId);
        return ResponseEntity.ok(cartItemDto);
    }

    @Operation(summary = "Remove cart item from a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> remove(@PathVariable Long cartItemId) {
        log.info("Removing cart item with id: {}", cartItemId);
        cartItemService.remove(cartItemId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get cart items by cart ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found cart items",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CartItemDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDto>> getByCartId(@PathVariable Long cartId) {
        log.info("Retrieving items for cart id: {}", cartId);
        List<CartItemDto> cartItems = cartItemService.getByCartId(cartId);
        return ResponseEntity.ok(cartItems);
    }

    @Operation(summary = "Update cart item quantity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item quantity updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartItemDto.class))}),
            @ApiResponse(responseCode = "404", description = "Cart item not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemDto> updateQuantity(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
        log.info("Updating quantity of cart item id: {} to {}", cartItemId, quantity);
        CartItemDto cartItemDto = cartItemService.updateQuantity(cartItemId, quantity);
        return ResponseEntity.ok(cartItemDto);
    }

    @ExceptionHandler(CartItemsNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(CartItemsNotFoundException ex) {
        log.error("Cart item not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartItemsInvalidArgumentException.class)
    public ResponseEntity<String> handleInvalidArgumentException(CartItemsInvalidArgumentException ex) {
        log.error("Cart item invalid argument exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}