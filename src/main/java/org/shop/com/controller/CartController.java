package org.shop.com.controller;

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
import org.springframework.web.context.request.WebRequest;

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

    @PostMapping
    public ResponseEntity<CartDto> createOrUpdateCart(@RequestBody CartCreateDto cartCreateDto) {
        log.debug("Request to create/update cart: {}", cartCreateDto);
        CartDto cartDto = cartService.createOrUpdateCart(cartCreateDto);
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable Long userId) {
        log.debug("Request to get cart for user ID: {}", userId);
        CartDto cartDto = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        log.debug("Request to delete cart with ID: {}", cartId);
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts() {
        log.debug("Request to get all carts");
        List<CartDto> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public final ResponseEntity<Object> handleCartNotFoundException(CartNotFoundException ex, WebRequest request) {
        log.error("Cart not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartInvalidArgumentException.class)
    public final ResponseEntity<Object> handleCartInvalidArgumentException(CartInvalidArgumentException ex, WebRequest request) {
        log.error("Cart invalid argument exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}