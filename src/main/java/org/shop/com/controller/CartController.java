package org.shop.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CartCreateDto;
import org.shop.com.dto.CartDto;
import org.shop.com.exceptions.CartInvalidArgumentException;
import org.shop.com.exceptions.CartNotFoundException;
import org.shop.com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/v1/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Create or update a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart created/updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CartDto> createOrUpdateCart(@RequestBody CartCreateDto cartCreateDto) {
        log.debug("Request to create/update cart: {}", cartCreateDto);
            CartDto cartDto = cartService.createOrUpdateCart(cartCreateDto);
            return ResponseEntity.ok(cartDto);
    }

    @Operation(summary = "Get cart by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cart",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable Long userId) {
        log.debug("Request to get cart for user ID: {}", userId);
        CartDto cartDto = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDto);
    }

    @Operation(summary = "Delete a cart by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        log.debug("Request to delete cart with ID: {}", cartId);
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all carts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all carts",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CartDto.class))) }),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts() {
        log.debug("Request to get all carts");
        List<CartDto> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public final ResponseEntity<Object> handleCartNotFoundException(CartNotFoundException ex) {
        log.error("Cart not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartInvalidArgumentException.class)
    public final ResponseEntity<Object> handleCartInvalidArgumentException(CartInvalidArgumentException ex) {
        log.error("Cart invalid argument exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}